package com.elearning.controller;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.elearning.entity.Category;
import com.elearning.entity.Course;
import com.elearning.entity.User;
import com.elearning.entity.Video;
import com.elearning.service.AdminServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminServiceImpl aservice;

	// Get course reports
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@GetMapping(path = "/coursereports")
		public List<Course> courseReports() {
			return aservice.courseStats();
		}
		// show all categories
		@PreAuthorize("hasAnyAuthority('admin','prime','user')")
		@GetMapping("/category")
		public ResponseEntity<List<Category>> AllCategory() {
			List<Category> li = aservice.getAllCategory();
			if(li.size()==0) {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT,"No category found!!!");
			}
			else{
				return ResponseEntity.status(HttpStatus.OK).body(li);
			}
		}
		// show category by id
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@GetMapping(value = "/category/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
		public ResponseEntity<Optional<Category>> CategoryById(@PathVariable int id) {
			 Optional<Category> c=aservice.getCategoryById(id);
			 if(c.isEmpty()) {
				 throw new ResponseStatusException(HttpStatus.NO_CONTENT,"No category found!!!");
			 }
			 else {
				 return  ResponseEntity.status(HttpStatus.OK).body(c); 
			 }
		}
		// add category
		@PostMapping("/category")
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		public  ResponseEntity<Boolean> addCategory(@RequestBody Category c) {
			boolean b= aservice.addCategory(c);
			 if(b==false) {
				 throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Category not addedd!!!");
			 }
			 else {
				 return  ResponseEntity.status(HttpStatus.OK).body(b); 
			 }
		}
		// delete category by id
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@DeleteMapping("/category/{id}")
	public ResponseEntity<Boolean> deleteCategory(@PathVariable int id) {
			Optional<Category> c=aservice.getCategoryById(id); 
			 if(c.isEmpty()) {
				 throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Given category not found, so cannot be deleted!!!");
			 }
			 else {
				 aservice.deleteCategory(id);
				 return  ResponseEntity.status(HttpStatus.OK).body(true); 
			 }
		}
		// update category by id
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@PutMapping("/category/{cat_id}")
		public ResponseEntity<Boolean> updateCategory(@RequestBody Category c, @PathVariable int cat_id) {
			c.setCategoryId(cat_id);
			Optional<Category> ctest=aservice.getCategoryById(cat_id);
			boolean b=aservice.updateCategory(c,ctest,cat_id);
			 if(b==false) {
				 throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Category not updated!!!");
			 }
			 else {
				 return  ResponseEntity.status(HttpStatus.OK).body(b); 
			 }
		}
		// total categories
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@GetMapping("/category/total")
		public ResponseEntity<Integer> totalCategory() {
			int c;
			c=(int) aservice.getCategoryCount();
			 if(c==0) {
				 throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Category count is '0'!!!");
			 }
			 else {
				 return  ResponseEntity.status(HttpStatus.OK).body(c); 
			 }
		}
		// get category name list
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@GetMapping("/category/category-name")
		public ResponseEntity<List<Object>> getAllCategoryName() {
			List<Object> catName = aservice.findCategoryName();
			if (catName == null) {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, " No categories present!!!");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(catName);
			}
		}
		// show all courses
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@GetMapping("/course")
		public ResponseEntity<List<Course>> AllCourse() {
			List<Course> li = aservice.getAllCourse();
			if(li.size()==0) {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT,"No course found!!!");
				}
				else{
					return ResponseEntity.status(HttpStatus.OK).body(li);
				}
		}
		// show course by id
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@GetMapping("/course/{id}")
	public ResponseEntity<Optional<Course>> CourseById(@PathVariable int id) {
			Optional<Course> c=aservice.getCourseById(id);
			 if(c.isEmpty()) {
				 throw new ResponseStatusException(HttpStatus.NO_CONTENT,"No course found!!!");
			 }
			 else {
				 return  ResponseEntity.status(HttpStatus.OK).body(c); 
			 }
		}
		// add course
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@PostMapping("/course/{cat_id}")
		public ResponseEntity<Boolean> addCourse(@RequestBody Course c, @PathVariable int cat_id) {
			boolean b=aservice.addCourse(c, cat_id);
			if(b==false) {
				throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Course not addedd!!!");
			 }
			 else {
				 return  ResponseEntity.status(HttpStatus.OK).body(b); 
			 }
		}
		// delete course by id
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@DeleteMapping("/course/{id}")
		public ResponseEntity<Boolean> deleteCourse(@PathVariable int id) {
			Optional<Course> c=aservice.getCourseById(id); 
			if(c.isEmpty()) {
				 throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Given course not found, so cannot be deleted!!!");
			 }
			 else {
				 aservice.deleteCourse(id);
				 return  ResponseEntity.status(HttpStatus.OK).body(true); 
			 }
		}
		// update category by id
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@PutMapping("/course/{co_id}/{cat_id}")
		public ResponseEntity<Boolean> updateCourse(@RequestBody Course c, @PathVariable int co_id,@PathVariable int cat_id) {
			c.setCourseId(co_id);
			Optional<Course> ctest=aservice.getCourseById(co_id);
			boolean b= aservice.updateCourse(c,ctest);
			if(b==false) {
				 throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Course not updated!!!");
			 }
			 else {
				 return  ResponseEntity.status(HttpStatus.OK).body(b); 
			 }
		}
		// total courses
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@GetMapping("/course/total")
		public ResponseEntity<Integer> totalCourses() {
			int c;
			c=(int) aservice.getCourseCount();
			 if(c==0) {
				 throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Course count is '0'!!!");
			 }
			 else {
				 return  ResponseEntity.status(HttpStatus.OK).body(c); 
			 }
		}
		// show all videos
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@GetMapping("/video")
		public ResponseEntity<List<Video>> AllVideos() {
			List<Video> li2 = aservice.getAllVideo();
			if(li2.size()==0) {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT,"No video found!!!");
				}
				else{
					return ResponseEntity.status(HttpStatus.OK).body(li2);
				}
		}
		// show video by id
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@GetMapping("/video/{id}")
		public ResponseEntity<Optional<Video>> VideoById(@PathVariable int id) {
			Optional<Video> v=aservice.getVideoById(id);
			 if(v.isEmpty()) {
				 throw new ResponseStatusException(HttpStatus.NO_CONTENT,"No video found!!!");
			 }
			 else {
				 return  ResponseEntity.status(HttpStatus.OK).body(v); 
			 }
		}
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@PostMapping("/video/{co_id}/{position}")
		public ResponseEntity<Boolean> addVideo(@RequestBody Video c, @PathVariable int co_id,
				@PathVariable String position) {
			boolean b = aservice.addVideo(c, co_id, position);
			if (b == false) {
				throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Video not addedd!!!");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(b);
			}
		}
		// delete video
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@DeleteMapping("/video/{id}")
	public ResponseEntity<Boolean> deleteVideo(@PathVariable int id) {
			Optional<Video> c=aservice.getVideoById(id); 
			if(c.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Given video not found, so cannot be deleted!!!");
			 }
			 else {
				 aservice.deleteVideo(id);
				 return  ResponseEntity.status(HttpStatus.OK).body(true); 
			 }
		}
		// update video by id
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@PutMapping("/video/{v_id}/{co_id}")
		public ResponseEntity<Boolean> updateVideo(@RequestBody Video v, @PathVariable int v_id,@PathVariable int co_id) {
			v.setVideoId(v_id);
			boolean b= aservice.updateVideo(v,co_id);
			if(b==false) {
				 throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Video not updated!!!");
			 }
			 else {
				 return  ResponseEntity.status(HttpStatus.OK).body(b); 
			 }
		}
		// total videos
		@PreAuthorize("hasAnyAuthority('admin','prime')")
		@GetMapping("/video/total")
		public ResponseEntity<Integer> totalVideos() {
			int c;
			c = (int) aservice.getVideoCount();
			if (c == 0) {
				 throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Video count is '0'!!!");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(c);
			}
		}
		// show all users.
		@PreAuthorize("hasAuthority('prime')")
		@GetMapping("/user")
		public ResponseEntity<List<User>> AllUsers() {
			List<User> li = aservice.getAllUser();
			if (li.size() == 0) {
				throw new ResponseStatusException(HttpStatus.OK,"No user found!!!");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(li);
			}
		}
		// show locked users
		@PreAuthorize("hasAuthority('prime')")
		@GetMapping(path = "/lockedusers")
		public ResponseEntity<List<User>> getLocked() {
			List<User> userList = aservice.getLockedAccount();
			if (userList.size() == 0) {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT,"No locked user found!!!");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(userList);
			}
		}
		// unlock the user
		@PreAuthorize("hasAuthority('prime')")
		@PutMapping(path = "/unlockuser/{u_id}")
		public ResponseEntity<Boolean> unlock(@PathVariable int u_id) throws AddressException, MessagingException {
			boolean b = aservice.unlocakAccount(u_id);
			if (b == false) {
				 throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"User not unlocked!!!");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(b);
			}
		}
		// lock the user
		@PreAuthorize("hasAuthority('prime')")
		@PutMapping(path = "/lockuser/{u_id}")
		public ResponseEntity<Boolean> lock(@PathVariable int u_id) {
			boolean b = aservice.lockAccount(u_id);
			if (b == false) {
				 throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"User not locked!!!");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(b);
			}
		}
		@PreAuthorize("hasAuthority('prime')")
		@GetMapping("/usercount")
		public long totalUsers() {
			return aservice.getUserCount();
		}
		@PreAuthorize("hasAuthority('prime')")
		@PutMapping("/password/{flag}") // we will take whole object of user first in UI then will send the updated
										// object
		public User changePassword(@RequestBody User user, @PathVariable boolean flag) {
			return aservice.changePassword(user.getPassword(), flag);
		}

}