package com.elearning.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.elearning.entity.Comment;
import com.elearning.entity.Course;
import com.elearning.entity.EnrolledCourses;
import com.elearning.entity.Feedback;
import com.elearning.entity.Profile;
import com.elearning.entity.User;
import com.elearning.entity.Video;
import com.elearning.service.UserService;
import com.elearning.service.UserServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import io.micrometer.core.ipc.http.HttpSender.Request;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	@Autowired
	UserService uservice = new UserServiceImpl();

	@PreAuthorize("hasAuthority('user')")
	@PostMapping("/comment")
	public ResponseEntity<String> postComment(@RequestParam int userid, @RequestParam int courseid,
			@RequestParam String comment_msg) {
		Comment count = uservice.addComment(userid, courseid, comment_msg);

		if (count == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot add comment");
		}
		return ResponseEntity.status(HttpStatus.OK).body("comment added");

	}

	@PreAuthorize("hasAuthority('user')")
	@PostMapping("/feedback")
	public ResponseEntity<String> postfeedback(@RequestParam int userid, @RequestParam int courseid,
			@RequestParam String feedback_msg, @RequestParam int rating) {
		Feedback count = uservice.addFeedback(userid, courseid, feedback_msg, rating);

		if (count == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot add feedback");
		}
		return ResponseEntity.status(HttpStatus.OK).body("feedback added");

	}

	@PreAuthorize("hasAuthority('user')")
	@DeleteMapping("/comment")
	public ResponseEntity<String> deleteComment(@RequestParam int commentid) {

		uservice.deleteComment(commentid);
		return null;

	}

	@PreAuthorize("hasAuthority('user')")
	@DeleteMapping("/feedback")
	public ResponseEntity<String> deletefeedback(@RequestParam int feedbackid) {

		uservice.deleteFeedback(feedbackid);
		return null;

	}

	@PreAuthorize("hasAuthority('user')")
	@PutMapping("/comment")
	public ResponseEntity<String> upateComment(@RequestParam int userid, @RequestParam int courseid,
			@RequestParam int commentid, @RequestParam String comment_msg) {
		Comment count = uservice.updateComment(userid, courseid, commentid, comment_msg);

		if (count == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot add comment");
		}
		return ResponseEntity.status(HttpStatus.OK).body("comment added");
	}

	@PreAuthorize("hasAuthority('user')")
	@PutMapping("/feedback")
	public ResponseEntity<String> upatefeedback(@RequestParam int userid, @RequestParam int courseid,
			@RequestParam int feedbackid, @RequestParam String feedback_msg) {
		Feedback count = uservice.updateFeedback(userid, courseid, feedbackid, feedback_msg);

		if (count == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot add feedback");
		}
		return ResponseEntity.status(HttpStatus.OK).body("feedback added");
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/comment")
	public List<Comment> fetchComment(@RequestParam int courseid) {
		return uservice.fetchComment(courseid);
	}

//	@GetMapping("/feedback")
//	public List<Comment> fetchFeedback(@RequestParam int feedbackid){
//		return uservice.fetchComment(feedbackid);
//	}
//	
	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/feedback")
	public List<Feedback> fetchFeedbackbyCourseID(@RequestParam int courseid) {
		return uservice.fetchFeedbacks(courseid);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/completedCourses")
	public List<Course> completedCourses(@RequestParam int userId) {
		return uservice.fetchcompletedCourses(userId);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/courses")
	public ResponseEntity<List<Course>> AllCourse() {
		List<Course> li = uservice.AllCourse();
		for (Course l : li) {
			System.out.println(l);
		}
		return ResponseEntity.status(HttpStatus.OK).body(li);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/videos")
	public ResponseEntity<List<Video>> getVideos(@RequestParam int courseid) {
		List<Video> li = uservice.getVideos(courseid);
		return ResponseEntity.status(HttpStatus.OK).body(li);
	}

	// show course by id
	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/course")
	public Optional<Course> CourseById(@RequestParam int courseid) {
		return uservice.getCourseById(courseid);
	}

	// total number of feedbacks
	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/feedbackcount")
	public int feedbackcount(@RequestParam int courseid) {
		return uservice.feedbackcount(courseid);
	}

	// total number of comments
	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/commentcount")
	public int commentcount(@RequestParam int courseid) {
		return uservice.commentcount(courseid);
	}
	

	@PreAuthorize("hasAuthority('user')")
	@PutMapping(path = "/like/{cid}/{uid}")
	public ResponseEntity<Boolean> like(@PathVariable int cid, @PathVariable int uid) {

		// 11 is the user id
		boolean status = uservice.like(uid, cid);
		if (status) {
			return  ResponseEntity.status(HttpStatus.OK).body(status); 
		}
		return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(status);
	}

	@PreAuthorize("hasAuthority('user')")
	@DeleteMapping(path = "/unlike/{cid}/{uid}")
	public String unlike(@PathVariable int cid, @PathVariable int uid) {

		// 11 is the user id
		boolean status = uservice.unlike(uid, cid);
		if (status) {
			return "Un Liked";
		}
		return "can not unlike";
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/isliked/{cid}/{uid}")
	public boolean isliked(@PathVariable int cid, @PathVariable int uid) {

		return uservice.isliked(cid, uid);

	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/isenrolled/{cid}/{uid}")
	public boolean isenrolled(@PathVariable int cid, @PathVariable int uid) {

		return uservice.isenrolled(cid, uid);

	}

	@PreAuthorize("hasAuthority('user')")
	@PutMapping(path = "enroll/{cid}/{uid}")
	public String enroll(@PathVariable int cid, @PathVariable int uid) {

		// 11 is the user id
		boolean status = uservice.Enroll(cid, uid);
		if (status) {
			return "Enrolled";
		}
		return "can not enroll";
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "/enrolledcourses/video/{cid}/{uid}")
	public List<Video> getVideo(@PathVariable int cid, @PathVariable int uid) {
		return uservice.getEnrolledCourseVideo(uid, cid);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "nextvideo/{cid}/{uid}/{vid}")
	public boolean nextVideo(@PathVariable int cid, @PathVariable int uid, @PathVariable int vid) {

		return uservice.nextVideo(cid, uid, vid);
	}

	@PreAuthorize("hasAuthority('user')")
	@PutMapping(path = "completevideo/{cid}/{uid}/{vid}")
	public boolean completeVideo(@PathVariable int cid, @PathVariable int uid, @PathVariable int vid) throws AddressException, MessagingException, IOException {

		return uservice.completeVideo(cid, uid, vid);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "iscompleted/{cid}/{uid}")
	public boolean completedcourse(@PathVariable int cid, @PathVariable int uid) {
		return uservice.isCourseCompleted(cid, uid);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "/generatePdf/{cid}/{uid}")
	public boolean generatepdf(@PathVariable int cid, @PathVariable int uid) {
		return uservice.generateCompeletionCerti(uid, cid);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "/regotp")
	public String regotp(@RequestParam int userid, HttpServletRequest request)
			throws AddressException, MessagingException {
		HttpSession session = request.getSession();
		int random = uservice.regotp(userid, session);
		return Integer.toString(random);
	}


	@PutMapping("/incrementfa/{username}")
	public int ifa(@PathVariable String username) throws AddressException, MessagingException {
		return uservice.incrementfailed(username);
	}

	@PreAuthorize("hasAuthority('user')")
	@PutMapping("/clearfa/{userid}")
	public boolean cfa(@PathVariable int userid) throws AddressException, MessagingException {
		return uservice.clearfalied(userid);

	}

	
	@GetMapping("/isLocked/{username}")
	public boolean isLocked(@PathVariable String username) {
		return uservice.isLocked(username);

	}


	@PostMapping(path = "/adduser")
	@ResponseStatus(code = HttpStatus.CREATED)
	public String createUser(@RequestBody User user) {
		return uservice.createUser(user);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "isActivated/{userid}")
	public boolean isActivated(@PathVariable int userid) {
		return uservice.isActivated(userid);
	}

	@PreAuthorize("hasAuthority('user')")
	@PutMapping(path = "/unlockuser/{userid}")
	public boolean unlock(@PathVariable int userid) throws AddressException, MessagingException {
		return uservice.unlocakAccount(userid);
	}

	@PreAuthorize("hasAuthority('user')")
	@PutMapping(path = "/lockuser/{userid}")
	public boolean lock(@PathVariable int userid) throws AddressException, MessagingException {
		return uservice.lockAccount(userid);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "/verifyreg/{status}/{username}")
	public boolean verifyreg(@PathVariable boolean status, @PathVariable String username) {
		return uservice.verifyOtp(username, status);
	}

	
	@GetMapping(path = "forgototp/{email}")
	public String forgototp(@PathVariable String email) throws AddressException, MessagingException {
		int random = uservice.forgototp(email);
		return Integer.toString(random);
	}

	
	@PutMapping(path = "changepass/{id}")
	public ResponseEntity<String> changepassword(@RequestBody User user, @PathVariable int id) {
		uservice.changepassword(user.getPassword(), id);
		return  ResponseEntity.status(HttpStatus.OK).body("changed");
	}
	
	@PutMapping(path = "forgotpass/{email}")
	public ResponseEntity<String> forgotpassword(@RequestBody User user, @PathVariable String email) {
		uservice.forgetpassword(user.getPassword(), email);
		return  ResponseEntity.status(HttpStatus.OK).body("changed");
	}
	
	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "sendcert/{courseid}/{userid}")
	public String sendcert(@PathVariable int courseid, @PathVariable int userid)
			throws AddressException, MessagingException, IOException {
		
		uservice.sendcert( userid, courseid);
		return "sent";

	}

	@PreAuthorize("hasAuthority('user')")
	@PostMapping("/profile/{userid}") // we have send userid while creating profile
	@ResponseStatus(code = HttpStatus.CREATED)
	public Profile createProfile(@PathVariable int userid, @RequestBody Profile profile) {
		// file from model attribute
//		
//		//profile.setUserImage(file.getInputStream());
//		try {
//			InputStream inputStream = file.getInputStream();
//			byte[] b = IOUtils.toByteArray(inputStream);
//			SerialBlob blob = new javax.sql.rowset.serial.SerialBlob(b);
//			profile.setUserImage(blob);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return uservice.createProfile(userid, profile);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/profile/{userid}")
	public Profile getProfileDetails(@PathVariable int userid) {
		return uservice.getProfileDetails(userid);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/isProfileCreated/{userid}")
	public boolean isPC(@PathVariable int userid) {
		return uservice.isProfileCreated(userid);
	}
	
	
	@GetMapping("/username/{username}")
	public boolean checkUsername(@PathVariable String username) {
		return uservice.checkUsername(username);
	}
	

	@GetMapping("/email/{email}")
	public boolean checkEmail(@PathVariable String email) {
		return uservice.checkEmail(email);
	}

	@PreAuthorize("hasAuthority('user')")
	@DeleteMapping("/delete/{id}")
	public boolean deleteUser(@PathVariable int id) {
		return uservice.deleteUser(id);
	}

	@PreAuthorize("hasAuthority('user')")
	@GetMapping("/coursesbycatId/{catId}")
	public List<Course> getCourseById(@PathVariable int catId) {
		return uservice.findCourseByCat(catId);
	}
	
	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "getcert/{courseid}/{userid}")
	public String getCerti(@PathVariable int courseid, @PathVariable int userid) {
		return uservice.getCerti(courseid, userid);
		
	}
	
	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path="enrolled/{userid}")
	public List<Course> getenrolled(@PathVariable int userid) {
		return uservice.getEnrolledCourse(userid);
		
	}
	
	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path="finished/{userid}")
	public List<Course> getFinishedCourse(@PathVariable int userid) {
		return uservice.finishedCourse(userid);
		
	}
	
	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path="enrolleddates/{userid}")
	public List<Date> getenrolledDates(@PathVariable int userid) {
		return uservice.getEDates(userid);
		
	}
	
	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path="finisheddates/{userid}")
	public List<Date> getFinishedCourseDates(@PathVariable int userid) {
		return uservice.getFDates(userid);
		
	}
	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path="certificates/{userid}")
	public List<String> getCerties(@PathVariable int userid) {
		return uservice.getCertiPaths(userid);
		
	}
	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "getVideoStatus/{courseid}/{userid}")
	public List<String> getVideoStatus(@PathVariable int courseid, @PathVariable int userid) {
		return uservice.getVideoStatus(userid, courseid);
		
	}
	@PreAuthorize("hasAuthority('user')")
	@GetMapping(path = "getFeedbackStatus/{courseid}/{userid}")
	public boolean getFeedbackStatus(@PathVariable int courseid, @PathVariable int userid) {
		return uservice.isFeedbackGiven(userid,courseid );
		
	}


}
