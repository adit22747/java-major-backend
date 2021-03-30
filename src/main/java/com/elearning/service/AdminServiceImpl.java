package com.elearning.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.elearning.email.EmailUtil;
import com.elearning.entity.Category;
import com.elearning.entity.Comment;
import com.elearning.entity.Course;
import com.elearning.entity.EnrolledCourseVideo;
import com.elearning.entity.EnrolledCourses;
import com.elearning.entity.Feedback;
import com.elearning.entity.PrimeAdmin;
import com.elearning.entity.User;
import com.elearning.entity.Video;
import com.elearning.repositiories.Categoryrepo;
import com.elearning.repositiories.CommentRepo;
import com.elearning.repositiories.CourseRepo;
import com.elearning.repositiories.EnrolledCourseRepo;
import com.elearning.repositiories.FeedbackRepo;
import com.elearning.repositiories.UserRepo;
import com.elearning.repositiories.VideoRepo;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	Categoryrepo cat;
	@Autowired
	CourseRepo cou;
	@Autowired
	VideoRepo vr;
	@Autowired
	FeedbackRepo fr;
	@Autowired
	CommentRepo cmtr;
	@Autowired
	EnrolledCourseRepo ecr;
	@Autowired
	UserRepo ur;

	@Override
	public List<Course> courseStats() {
		List<Course> courses=cou.findAll();
		List<Comment> comments=cmtr.findAll();
		List<Feedback> feedbacks=fr.findAll();
		List<EnrolledCourses> ecs=ecr.findAll();
		for (int i=0;i<courses.size();i++) {
			courses.get(i).setVideosize(courses.get(i).getVideo().size());
			for(int j=0;j<comments.size();j++) {
				if(courses.get(i).getCourseId()==comments.get(j).getCourse().getCourseId() ) {
					courses.get(i).setTotalcomment(courses.get(i).getTotalcomment()+1);
				}
			}
			float avgrating = 0;
			for (Feedback f :feedbacks) {
				avgrating = avgrating + f.getRating();
			}
			int rating = (int) (avgrating / (courses.get(i).getFeedbacks().size()));
			courses.get(i).setRating(rating);
			for(int l=0;l<ecs.size();l++) {
				if(courses.get(i).getCourseId()==ecs.get(l).getCourse().getCourseId() ) {
					courses.get(i).setEnrollments(courses.get(i).getEnrollments()+1);
					System.out.println(courses.get(i).getTotalcomment());
				}
			}
		}
		return courses;
	}
	
	@Override
	public List<Category> getAllCategory() {
		return cat.findAll();
	}

	@Override
	public Optional<Category> getCategoryById(int id) {
		return cat.findById(id);
	}

	@Override
	public boolean addCategory(Category c) {
		return cat.save(c) != null;
	}

	@Override
	public void deleteCategory(int id) {
		cat.deleteById(id);

	}

	@Override
	public boolean updateCategory(Category c, Optional<Category> ctest, int id) {
		List<Course> cotest = ctest.get().getCourses();
		c.setCourses(cotest);
		return cat.save(c) != null;
	}

	@Override
	public List<Course> getAllCourse() {
		return cou.findAll();
	}

	@Override
	public Optional<Course> getCourseById(int id) {
		return cou.findById(id);
	}

	@Override
	public boolean addCourse(Course c, int id) {
		long millis = System.currentTimeMillis();
		Date date = new java.sql.Date(millis);
		c.setLastupdated(date);
		Optional<Category> cate = cat.findById(id);
		List<Course> courses = cate.get().getCourses();
		courses.add(c);
		cate.get().setCourses(courses);
		cat.save(cate.get());
		return true;
	}

	@Override
	public void deleteCourse(int i) {
		cou.deleteById(i);

	}

	@Override
	public boolean updateCourse(Course c, Optional<Course> ctest) {
		List<Video> video = ctest.get().getVideo();
		String cat_name = ctest.get().getCategory();
		Category category = cat.findByCategoryName(cat_name);
		Optional<Course> course = cou.findById(c.getCourseId());
		if(c.getCoursePrice()!=course.get().getCoursePrice()) {
			long millis = System.currentTimeMillis();
			Date date = new java.sql.Date(millis);
			c.setLastupdated(date);
			
		}
//		if(c.getCoursePrice()!=) {
//			
//		}
//		long millis = System.currentTimeMillis();
//		Date date = new java.sql.Date(millis);
		c.setCategory(category);
		c.setVideo(video);
		return cou.save(c) != null;
	}

	@Override
	public boolean addVideo(Video v,int id,String pos) {
		Optional<Course> co=cou.findById(id);
		List<Video>videos =vr.findAllByCourseOrderBySrNoAsc(co.get());
		List<Integer> srnos = vr.getVideoSrNo(id);
		if("last".equals(pos)){
			int size=videos.size();
			if(size==0) {
				v.setSrNo(1);
				
			}
			else {
				v.setSrNo(videos.get(size-1).getSrNo()+1);
			}
			
		}
		else{
			int Position=Integer.parseInt(pos);
			System.out.println(Position);
			for(int i=Position ;i<videos.size()-1;i++) {
				
					//System.out.println(srnos.get(i) );
					
					 videos.get(i).setSrNo(srnos.get(i+1));
					 System.out.println(videos.get(i).getSrNo());
			}
			videos.get(videos.size()-1).setSrNo(srnos.get(srnos.size()-1)+1 );
	 		v.setSrNo(srnos.get(Position));
		}
			
		
		
 		videos.add(v);
 		co.get().setVideo(videos);
 		long millis = System.currentTimeMillis();
 		Date date = new java.sql.Date(millis);
 		co.get().setLastupdated(date);
 		cou.save(co.get());
 		addinEcv(id, v);
		return true;
 		
	}

	@Override
	public List<Video> getAllVideo() {
		return vr.findAll();
	}

	@Override
	public Optional<Video> getVideoById(int id) {
		return vr.findById(id);
	}

	@Override
	public boolean updateVideo(Video v, int id) {
		Optional<Video> video = vr.findById(v.getVideoId());
		Course course = cou.findByCourseName(video.get().getCourse());
		v.setSrNo(video.get().getSrNo());
		v.setCourse(course);
		vr.save(v);
		return true;
	}

	@Override
	public void deleteVideo(int i) {
		Optional<Video> video = vr.findById(i);
		Course course = cou.findByCourseName(video.get().getCourse());
		long millis = System.currentTimeMillis();
 		Date date = new java.sql.Date(millis);
 		course.setLastupdated(date);
 		cou.save(course);
		
		vr.deleteById(i);

	}

	@Override
	public long getCategoryCount() {
		return cat.count();
	}

	@Override
	public long getCourseCount() {
		return cou.count();
	}

	@Override
	public long getVideoCount() {
		return cou.count();
	}

	@Override
	public boolean lockAccount(int uid) {
		Optional<User> user = ur.findById(uid);
		user.get().setLocked(true);
		ur.save(user.get());
		return true;
	}

	@Override
	public boolean unlocakAccount(int uid) throws AddressException, MessagingException {
		Optional<User> user = ur.findById(uid);
		user.get().setLocked(false);
		user.get().setFailedattempts(0);
		ur.save(user.get());
		EmailUtil eu = new EmailUtil();
		eu.sendEmail(user.get().getEmail(), "On your request , your account with "+user.get().getUsername()+" - username has been unlocked. Now you can login to the Application");
		
		return true;
	}

	@Override
	public List<User> getLockedAccount() {
		return ur.getLockedUsers();
	}

	@Override
	public List<User> getAllUser() {
		return ur.findAll();
	}
	@Override
	public long getUserCount() {
		return ur.count();
	}

	@Override
	public List<Object> findCategoryName(){
		return cat.findCategoryName();
	}
	@Override
	public User changePassword(String password, boolean flag) {
		User user;
		PrimeAdmin prime;
		if(flag) {
			 prime = (PrimeAdmin) ur.findByUsername("primeadmin");
			 prime.setPassword(new BCryptPasswordEncoder().encode(password));
			 return  ur.save(prime);
			
		}
		else {
			 user = ur.findByUsername("admin");
			 user.setPassword(new BCryptPasswordEncoder().encode(password));
			 return  ur.save(user);
		}
}

	
	

	@Override
	public boolean addinEcv(int cid, Video v) {
		// TODO Auto-generated method stub
		Optional<Course> co = cou.findById(cid);
		List<EnrolledCourses> ecs = ecr.findAllByCourse(co.get());
		List<Video> videos=co.get().getVideo();
		if(videos.size()!=0) {
			Video Video = videos.get(videos.size()-1);
			EnrolledCourseVideo ecv=new EnrolledCourseVideo(0, false, Video, null);
			if(ecs.size()!=0) {
				for(int i=0;i<ecs.size();i++) {
					List<EnrolledCourseVideo> ecvs = ecs.get(i).getEcvideo();
					ecvs.add(ecv);
					ecr.save(ecs.get(i));
				}
			}
		}
		return false;
	}

	
	
}