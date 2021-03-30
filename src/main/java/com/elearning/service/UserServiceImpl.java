package com.elearning.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.elearning.email.EmailUtil;
import com.elearning.email.PdfMail;
import com.elearning.entity.Category;
import com.elearning.entity.Certificate;
import com.elearning.entity.Comment;
import com.elearning.entity.Course;
import com.elearning.entity.EnrolledCourseVideo;
import com.elearning.entity.EnrolledCourses;
import com.elearning.entity.Feedback;
import com.elearning.entity.Like;
import com.elearning.entity.Profile;
import com.elearning.entity.User;
import com.elearning.entity.Video;
import com.elearning.repositiories.Categoryrepo;
import com.elearning.repositiories.CertiRepo;
import com.elearning.repositiories.CommentRepo;
import com.elearning.repositiories.CourseRepo;
import com.elearning.repositiories.EnrolledCourseRepo;
import com.elearning.repositiories.EnrolledCourseVideoRepo;
import com.elearning.repositiories.FeedbackRepo;
import com.elearning.repositiories.LikeRepo;
import com.elearning.repositiories.ProfileRepo;
import com.elearning.repositiories.UserRepo;
import com.elearning.repositiories.VideoRepo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	CommentRepo comr;

	@Autowired
	UserRepo ur;

	@Autowired
	CourseRepo cr;

	@Autowired
	EnrolledCourseRepo ecr;

	@Autowired
	FeedbackRepo fr;

	@Autowired
	VideoRepo vr;

	@Autowired
	LikeRepo lr;

	@Autowired
	EnrolledCourseVideoRepo ecvr;

	@Autowired
	ProfileRepo pfr;
	
	@Autowired
	CertiRepo ctr;
	
	@Autowired
	Categoryrepo catr;

	@Override
	public Comment addComment(int userID, int courseID, String msg) {

		Optional<User> userD = ur.findById(userID);

		Optional<Course> courseD = cr.findById(courseID);
		if (ecr.findAllByUserAndCourse(userD.get(), courseD.get()).size() == 1) {
			Comment comment = new Comment(msg);
			comment.setUser(userD.get());
			comment.setCourse(courseD.get());
			return comr.save(comment);
		}
		return null;

	}

	@Override
	public boolean deleteComment(int commentid) {

		comr.deleteById(commentid);
		return true;
	}

	@Override
	public Comment updateComment(int userid, int courseid, int commentid, String comment_msg) {
		Optional<User> userD = ur.findById(userid);

		Optional<Course> courseD = cr.findById(courseid);
		if (ecr.findAllByUserAndCourse(userD.get(), courseD.get()).size() == 1) {
			Comment comment = new Comment(commentid, comment_msg);
			comment.setUser(userD.get());
			comment.setCourse(courseD.get());
			return comr.save(comment);
		}
		return null;
	}

	@Override
	public List<Comment> fetchComment(int id) {

		List<Comment> comments = comr.fetchComment(id);
		return comments;

	}

	@Override
	public Feedback addFeedback(int uid, int cid, String feedback, int rating) {
		Optional<User> userD = ur.findById(uid);
		Optional<Course> courseD = cr.findById(cid);
		boolean value = isCourseCompleted(cid, uid);
		System.out.println(value);
//		System.out.println(userD.get().getUserId());
//		System.out.println(courseD.get().getCourseId());
//		System.out.println(fr.findAllByUserAndCourse(userD.get(), courseD.get()).size());
		if (fr.findAllByUserAndCourse(userD.get(), courseD.get()).size() == 0) {
			if (value == true) {
				Optional<Course> c = cr.findById(cid);
				Optional<User> u = ur.findById(uid);
				Feedback fb = new Feedback(feedback, c.get(), u.get(), rating);
				// TODO Auto-generated method stub

				return fr.save(fb);
			}
		} else {
			System.out.println("do not add");
		}
//		if (fr.findAllByUserAndCourse(userD.get(), courseD.get()).size() == 1) {
//			if(value == true) {
//				Optional<Course> c = cr.findById(cid);
//				Optional<User> u = ur.findById(uid);
//				Feedback fb = new Feedback(feedback, c.get(), u.get());
//				// TODO Auto-generated method stub
//
//				return fr.save(fb);
//			}
//		}

		return null;
	}

	@Override
	public boolean isCourseCompleted(int cid, int uid) {
		Optional<Course> c = cr.findById(cid);
		Optional<User> u = ur.findById(uid);
		EnrolledCourses ec = ecr.findByUserAndCourse(u.get(), c.get());
		if(ec!=null) {
			if (ec.getStartDate() != null & ec.getEndDate() != null) {
				return true;
		}
		
		}
		return false;
	}

	@Override
	public boolean deleteFeedback(int feedbackid) {
		fr.deleteById(feedbackid);
		return true;
	}

	@Override
	public Feedback updateFeedback(int userid, int courseid, int feedbackid, String feedback_msg) {
		Optional<User> userD = ur.findById(userid);

		Optional<Course> courseD = cr.findById(courseid);
		if (ecr.findAllByUserAndCourse(userD.get(), courseD.get()).size() == 1) {
			Feedback feedback = new Feedback(feedbackid, feedback_msg);
			feedback.setUser(userD.get());
			feedback.setCourse(courseD.get());
			return fr.save(feedback);
		}
		return null;
	}

	@Override
	public List<Feedback> fetchFeedbacks(int cid) {
		Optional<Course> c = cr.findById(cid);
		return fr.findAllByCourse(c.get());
	}

	@Override
	public List<Course> fetchcompletedCourses(int userId) {
		Optional<User> userD = ur.findById(userId);
		List<Course> ecrs = cr.getFinsihedCourses(userId);
		return ecrs;
//		/ return  ecrs.stream().filter(a->a.!=null && a.getStartDate()!=null).collect(Collectors.toList());

	}

	@Override
	public List<Course> AllCourse() {
		return setAvgRating(cr.findAll());
	}

	@Override
	public List<Course> setAvgRating(List<Course> courses) {
		for (Course c : courses) {
			float avgrating = 0;
			for (Feedback f : c.getFeedbacks()) {
				avgrating = avgrating + f.getRating();
			}
			int rating = (int) (avgrating / (c.getFeedbacks().size()));
			c.setRating(rating);
		}
		return courses;
	}

	@Override
	public Optional<Course> getCourseById(int courseid) {

		return setAvgRating(cr.findById(courseid));
	}

	@Override
	public Optional<Course> setAvgRating(Optional<Course> c) {

		float avgrating = 0;
		for (Feedback f : c.get().getFeedbacks()) {
			avgrating = avgrating + f.getRating();
		}
		int rating = (int) (avgrating / (c.get().getFeedbacks().size()));
		c.get().setRating(rating);
		return c;
	}

	@Override
	public int feedbackcount(int courseid) {
		Optional<Course> course = cr.findById(courseid);
		return course.get().getFeedbacks().size();
	}

	@Override
	public int commentcount(int courseid) {
		Optional<Course> course = cr.findById(courseid);
		return course.get().getComments().size();
	}

	@Override
	public List<Video> getVideos(int courseId) {
//		System.out.println("inside impl"); 
//		return vr.findAll();
		Optional<Course> course = cr.findById(courseId);
		return vr.findAllByCourse(course.get());
	}

	@Override
	public boolean like(int uid, int cid) {

		Optional<Course> c = cr.findById(cid);
		Optional<User> u = ur.findById(uid);
		Like IfLike = lr.findByCourseAndUser(c.get(), u.get());
		if (IfLike == null) {
			Like like = new Like(c.get(), u.get());
			int likes = c.get().getLikes();
			c.get().setLikes(++likes);
			cr.save(c.get());
			lr.save(like);
			return true;
		}

		return false;

	}

	@Override
	public boolean unlike(int uid, int cid) {

		Optional<Course> c = cr.findById(cid);
		Optional<User> u = ur.findById(uid);
		Like IfLike = lr.findByCourseAndUser(c.get(), u.get());
		if (IfLike != null) {
			int likes = c.get().getLikes();
			c.get().setLikes(--likes);
			lr.deleteById(IfLike.getLikeId());
			cr.save(c.get());
			return true;
		}
		return false;
//		Optional<Like> IfLike=lr.findById(likeid);
//		if(IfLike.get()!=null) {
//			Optional<Course> c=cr.findById(cid);
//			int likes=c.get().getLikes();c.get().setLikes(--likes);
//			cr.save(c.get());
//			lr.deleteById(likeid);
//			
//			return true;
//			
//		}
//		return false;

	}

	@Override
	public boolean isliked(int cid, int uid) {
		Optional<Course> c = cr.findById(cid);
		Optional<User> u = ur.findById(uid);
		Like IfLike = lr.findByCourseAndUser(c.get(), u.get());
		if (IfLike == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean Enroll(int cid, int uid) {
		Optional<Course> course = cr.findById(cid);
		Optional<User> u = ur.findById(uid);
		List<Video> videos = vr.findAllByCourse(course.get());
		List<EnrolledCourseVideo> ecvideos = new ArrayList<>();
		for (Video video : videos) {
			EnrolledCourseVideo ecv = new EnrolledCourseVideo(0, false, video, null);
			ecvideos.add(ecv);

		}

		long millis = System.currentTimeMillis();
		Date date = new java.sql.Date(millis);
		EnrolledCourses ec = new EnrolledCourses(date, null, u.get(), course.get(), ecvideos);
		ecr.save(ec);
		return true;
	}

	@Override
	public boolean isenrolled(int cid, int uid) {
		Optional<Course> c = cr.findById(cid);
		Optional<User> u = ur.findById(uid);
		List<EnrolledCourses> Ifenrolled = ecr.findByCourseAndUser(c.get(), u.get());
		if (Ifenrolled.size() == 0) {
			return false;
		}
		return true;
	}
	
//	@Override
//	public List<Course> enrolled(int uid) {
//		Optional<User> u = ur.findById(uid);
//		List<Course> Ifenrolled = cr.enrolledcourse(u.get().getUserId());
//		System.out.println(Ifenrolled.size());
//		return Ifenrolled;
//	}

//	@Override
//	public List<Course> getEnrolledCourse(int uid) {
//		// TODO Auto-generated method stub
////		return cr.getEnrolledCourses(uid);
//		
//	}

	@Override
	public List<Video> getEnrolledCourseVideo(int uid, int cid) {
		// TODO Auto-generated method stub
		List<Video> videos = vr.getVideo(uid, cid);
		System.out.println(videos.size());
		return videos;
	}

	@Override
	public boolean nextVideo(int cid, int uid, int vid) {
		// TODO Auto-generated method stub
		Optional<Course> course = cr.findById(cid);
		Optional<Video> video = vr.findById(vid);
		Optional<User> u = ur.findById(uid);
		EnrolledCourses ec = ecr.findByUserAndCourse(u.get(), course.get());
		int ecid = ec.getEcourseId();
		List<EnrolledCourseVideo> ecvs = ecvr.findAllByEc(ec);
		for (int i = 0; i < ecvs.size(); i++) {
			if (ecvs.get(i).getVideo() == vid) {
				if (i == 0) {
					return true;
				}
				if (ecvs.get(i - 1).isCompleted() == true) {
					return true;
				}
				return false;
			}

		}
		return false;
	}

	@Override
	public boolean completeVideo(int cid, int uid, int vid) throws AddressException, MessagingException, IOException {
		Optional<Course> course = cr.findById(cid);
		Optional<Video> video = vr.findById(vid);
		Optional<User> u = ur.findById(uid);
		EnrolledCourses ec = ecr.findByUserAndCourse(u.get(), course.get());
		List<EnrolledCourseVideo> ecvs = ec.getEcvideo();
		for (int i = 0; i < ecvs.size(); i++) {
			if (ecvs.get(i).getVideo() == vid) {
				ecvs.get(i).setCompleted(true);
				if (i == ecvs.size() - 1) {
					long millis = System.currentTimeMillis();
					Date date = new java.sql.Date(millis);
					ec.setEndDate(date);
					ec.setEcvideo(ecvs);
					//ecvr.deleteAllByEc(ec);
					ecr.save(ec);
					generateCompeletionCerti(uid, cid);
//					Certificate certi1=ctr.findByUserAndCourse(u.get(), course.get());
//					PdfMail pd = new PdfMail();
//					pd.sendEmail(u.get().getEmail(), certi1.getCertiPath());
//					for(i=0;i<ec.getEcvideo().size();i++) {
//						int id=ec.getEcvideo().get(i).getEcvId();
//						ecvr.deleteById(id);
//						System.out.println(ec.getEcvideo().get(i).getEcvId());
//					
//					}
//					//ecvr.deleteAllByEc(ec);
//					ecvr.deleteById(292);
//					ecvr.deleteById(293);
				ecvr.DeleteEcv(ec.getEcourseId());
					return true;

				}
			}

		}
		ec.setEcvideo(ecvs);
		ecr.save(ec);

		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean generateCompeletionCerti(int uid, int cid) {
		// TODO Auto-generated method stub

		Optional<User> user = ur.findById(uid);
		Profile profile = pfr.findByUser(user.get());
		Optional<Course> course = cr.findById(cid);
		System.out.println(profile.getFullName());
		System.out.println(course.get().getCourseName());
		EnrolledCourses ec = ecr.findByUserAndCourse(user.get(), course.get());
		Date enddate = ec.getEndDate();
		System.out.println(ec.getEndDate());
		String pdfname = user.get().getUsername() + course.get().getCourseName();
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("D:/java-g6-security-ui-main/pdf/"+pdfname + ".pdf"));
			document.open();
			Paragraph right = new Paragraph(Element.ALIGN_RIGHT);
			Paragraph left = new Paragraph(Element.ALIGN_LEFT);
			Font font = FontFactory.getFont(FontFactory.COURIER_BOLD, 30, BaseColor.BLACK);
			Chunk chunk = new Chunk("Certificate Of Completion", font);
			Paragraph preface = new Paragraph(chunk);
			preface.setAlignment(Element.ALIGN_CENTER);
			document.add(preface);

			Image img = Image.getInstance("D:/java-g6-security-ui-main/java-g6-security-ui-main/src/assets/"+course.get().getCourseLogo());

			right.add(img);

			Font font1 = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 15, BaseColor.GRAY);
			Font font2 = FontFactory.getFont(FontFactory.TIMES_BOLD, 25, BaseColor.DARK_GRAY);
			Chunk chunk1 = new Chunk("This is to Certify that" + "\n", font1);
			Chunk chunk2 = new Chunk(profile.getFullName() + "\n", font2);
			Chunk chunk3 = new Chunk(
					"has successfully completed " + course.get().getCourseName() + " course on date " + "\n", font1);
			Chunk chunk4 = new Chunk("'" + enddate + "'");

			PdfPCell leftcell = new PdfPCell();
			leftcell.addElement(chunk1);
			leftcell.addElement(chunk2);
			leftcell.addElement(chunk3);
			leftcell.addElement(chunk4);
			PdfPCell rightcell = new PdfPCell(img);
			leftcell.setBorder(Rectangle.NO_BORDER);
			rightcell.setBorder(Rectangle.NO_BORDER);
			leftcell.setPaddingTop(100);
			rightcell.setPaddingTop(100);
			leftcell.setVerticalAlignment(5);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.addCell(leftcell);
			table.addCell(rightcell);
			table.setPaddingTop(2000);

			document.add(table);
			Image cybage = Image.getInstance("Cybage-e-learning-logo.png");
			cybage.setAlignment(Element.ALIGN_CENTER);
			cybage.setBackgroundColor(BaseColor.BLUE);
			document.add(cybage);

			document.close();
			Certificate certi1=ctr.findByUserAndCourse(user.get(), course.get());
			if(certi1==null) {
				Certificate certi = new Certificate(pdfname+".pdf", course.get(), user.get());
				ctr.save(certi);
			}
			else {
				certi1.setCertiPath(pdfname+".pdf");
				ctr.save(certi1);

			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	@Override
	public int regotp(int userid,HttpSession session) throws AddressException, MessagingException {
		Optional<User> u = ur.findById(userid);
		int random = (int) (Math.random() * 100000);
		if(session != null) session.setAttribute(u.get().getUsername(),random);
		System.out.println(session.getAttribute("adit"));
		System.out.println("insdie sssssss");
		String userEmail=u.get().getEmail();
		EmailUtil eu = new EmailUtil();
		eu.sendEmail(userEmail, random+ " ");
		return random;
	}

	@Override
	public boolean verifyOtp(String username,boolean status) {
		// TODO Auto-generated method stub
		if(status) {
			System.out.println(status);
			User user = ur.findByUsername(username);
			user.setActivated(status);
			ur.save(user);
			return true;
		}
		System.out.println("inside false");
		return false;
		
	}
	
	

	@Override
	public boolean lockAccount(int uid) throws AddressException, MessagingException {
		// TODO Auto-generated method stub
		Optional<User> user=ur.findById(uid);
		user.get().setLocked(true);
		ur.save(user.get());
		EmailUtil eu = new EmailUtil();
		eu.sendEmail(user.get().getEmail(), "your account with "+user.get().getUsername()+" - username has been locked due to three invalid login attmepts");
		
		
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean unlocakAccount(int uid) throws AddressException, MessagingException {
		Optional<User> user=ur.findById(uid);
		user.get().setLocked(false);
		user.get().setFailedattempts(0);
		ur.save(user.get());
		EmailUtil eu = new EmailUtil();
		eu.sendEmail(user.get().getEmail(), "On your request , your account with "+user.get().getUsername()+" - username has been unlocked. Now you can login to the Application");
		
		
		// TODO Auto-generated method stub
		return true;
	}
	

	@Override
	public int incrementfailed(String username) throws AddressException, MessagingException {
		// TODO Auto-generated method stub
		User user = ur.findByUsername(username);
		if(user!=null) {
			if(user.getFailedattempts()==2) {
				user.setLocked(true);
			}
			if(user.getFailedattempts()<3) {
				int temp=user.getFailedattempts();
				user.setFailedattempts(++temp);
				ur.save(user);
			}
			return user.getFailedattempts();
			
		}
		
		
		return -1;
	}

	@Override
	public boolean clearfalied(int uid) throws AddressException, MessagingException {
		
		Optional<User> user = ur.findById(uid);
		user.get().setFailedattempts(0);
		user.get().setLocked(false);
		ur.save(user.get());
		
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isLocked(String username) {
		// TODO Auto-generated method stub
		User user = ur.findByUsername(username);
		if(user==null) {
			return false;
		}
		return user.isLocked();
	}
	
	
	@Override
	public String createUser(User user) {
		Profile p=new Profile();
		User u=new User(user.getUserId(), user.getUsername(), user.getEmail(), new BCryptPasswordEncoder().encode(user.getPassword()), false, false, "user");
		p.setUser(u);
		u.setProfile(p);
		ur.save(u);
		return "successfully created user";
	}


	@Override
	public boolean isActivated(int uid) {
		// TODO Auto-generated method stub
		Optional<User> user = ur.findById(uid);
		return user.get().isActivated();
	}

	@Override
	public int forgototp(String email) throws AddressException, MessagingException {
		User u = ur.findByEmail(email);
		int random = (int) (Math.random() * 100000);
		EmailUtil eu = new EmailUtil();
		eu.sendEmail(u.getEmail(), random+ " ");
		return random;
	}

	@Override
	public void changepassword(String password, int userid) {
		// TODO Auto-generated method stub
		Optional<User> u = ur.findById(userid);
		u.get().setPassword(new BCryptPasswordEncoder().encode(password));
		ur.save(u.get());
	}

	@Override
	public void sendcert(int userid,int courseid) throws AddressException, MessagingException, IOException {
		// TODO Auto-generated method stub
		Optional<User> u = ur.findById(userid);
		Optional<Course> c = cr.findById(courseid);
		Certificate cert = ctr.findByUserAndCourse(u.get(),c.get());
		PdfMail eu = new PdfMail();
		eu.sendEmail(u.get().getEmail(),cert.getCertiPath());
		
	}
	@Override
	public Profile createProfile(int userid, Profile profile) {
		Optional<User> user = ur.findUserDetails(userid);
		Profile pp = pfr.findByUser(user.get());
		pp.setBirthdate(profile.getBirthdate());
		pp.setFullName(profile.getFullName());
		pp.setGender(profile.getGender());
		pp.setContact(profile.getContact());
		pp.setSearchOccupation(profile.getSearchOccupation());
		//pp.setUserImage(profile.getUserImage());
		return pfr.save(pp);
	}
	@Override
	public boolean isProfileCreated(int uid) {
		// TODO Auto-generated method stub
		Optional<User> user = ur.findById(uid);
		Profile profile = pfr.findByUser(user.get());
		if(profile.getFullName()!=null) return true;
		return false;
	}


	@Override
	public Profile getProfileDetails(int userid) {
		Optional<User> user = ur.findById(userid);
		Profile profile = pfr.findByUser(user.get());
		return profile;
	}
	
	public boolean checkUsername(String username) {
		if(ur.checkUserName(username) == null) {
			return false;
		}
		return true;
	}
	public boolean checkEmail(String email) {
		if(ur.checkEmail(email) == null) {
			return false;
		}
		return true;
	}

	
	@Override
	public boolean deleteUser(int id) {
		
		Profile profile = pfr.findByUser(ur.findById(id).get());
		pfr.deleteById(profile.getProfileId());
		ur.deleteById(id);
		return false;
	}

	@Override
	public List<Course> findCourseByCat(int catid) {
		// TODO Auto-generated method stub
		Optional<Category> category = catr.findById(catid);
		return cr.findAllByCategory(category.get());
	}

	@Override
	public String getCerti(int courseid, int userid) {
		// TODO Auto-generated method stub
		Optional<Course> course = cr.findById(courseid);
		Optional<User> user = ur.findById(userid);
		Certificate certi = ctr.findByUserAndCourse(user.get(), course.get());
		
		return certi.getCertiPath();
	}

	@Override
	public List<Course> enrolled(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Course> finishedCourse(int uid) {
//		List<Integer> ecids = ecr.getEnrollmentCourseId(uid);
//		//List<Course> ecs = cr.getFinsihedCourses(uid);
//		List<EnrolledCourses> ecs = ecr.findAllByUser(ur.findById(uid).get());
//        List<Course> coursList = new ArrayList<Course>();
//		for(int i=0;i<ecs.size();i++) {
//			Course c = cr.findById(ecids.get(i)).get();
//			c.setEnddate(ecs.get(i).getEndDate());
//			coursList.add(c);
//		}
//		return coursList;
//		
		return cr.getFinsihedCourses(uid);
	}
	
	@Override
	public List<Date> getEDates(int uid) {
		// TODO Auto-generated method stub
		return ecr.getEnrollmentCourseDates(uid);
	}

	@Override
	public List<Date> getFDates(int uid) {
		// TODO Auto-generated method stub
		return ecr.getFinishedCourseDates(uid);
	}

	@Override
	public List<String> getCertiPaths(int uid) {
		// TODO Auto-generated method stub
		return ctr.getCertipaths(uid);
	}

	@Override
	public List<String> getVideoStatus(int uid, int cid) {
		// TODO Auto-generated method stub
		EnrolledCourses ec = ecr.findByUserAndCourse(ur.findById(uid).get(),cr.findById(cid).get());
		List<Boolean> videostatus = ecvr.videoStatus(ec.getEcourseId());
		List<String> ecVideoStatus=new ArrayList<>();
		for(int i=0;i<videostatus.size();i++) {
			if(videostatus.get(i)) {
				ecVideoStatus.add("Watched");
			}else {
				ecVideoStatus.add("Not Watched");
			}
		}
		return ecVideoStatus;
	}
	
	@Override
	public List<Course> getEnrolledCourse(int uid) {
		// TODO Auto-generated method stub
		List<Course> cs = cr.getEnrolledCourses(uid);
		List<Integer> ecids=ecr.getEnrollmentCourseIds(uid);

		if(cs.size() != 0) {
			for(int i=0;i<cs.size();i++) {
				int completed=ecvr.noOfCompletedVideo(ecids.get(i));
				System.out.println(completed);
				if(cs.get(i).getVideo().size()==0) {
					cs.get(i).setProgress(100);
				}
				else {
					cs.get(i).setProgress((completed*100/cs.get(i).getVideo().size()));


				}



			}
		}
		return cr.getEnrolledCourses(uid);
	}
	public boolean isFeedbackGiven(int uid,int cid) {
		
		
		List<Feedback> feedbacks = fr.findAllByUserAndCourse(ur.findById(uid).get(),cr.findById(cid).get());
		if(feedbacks.size()==1) {
			return true;
		}
		return false;
	}

	

}
