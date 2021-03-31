package com.elearning.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.plaf.ColorUIResource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import com.elearning.entity.*;
import com.elearning.repositiories.*;
@ExtendWith(MockitoExtension.class)
class AdminServiceImplMockTest {
	@Mock
	public Categoryrepo catRepo;
	@Mock
	public CourseRepo courseRepo;
	@Mock
	public VideoRepo videoRepo;
	@InjectMocks
	private AdminServiceImpl as;
	
	@Test
	void testGetAllCategory() {
		List<Course> course = null;
		List<Category> category = new ArrayList<Category>();
		category.add(new Category("devloping", "description of category", "abc.jpeg", course));
		category.add(new Category("designing", "description of category", "abc.jpeg", course));
		category.add(new Category("Testing", "description of category", "abc.jpeg", course));
		when(catRepo.findAll()).thenReturn(category);
		List<Category> result = as.getAllCategory();
		assertEquals(3, result.size());
	}
//	@Test
//	void testCourseStats() {
//		fail("Not yet implemented");
//	}
	@Test
	void testGetCategoryById() {
		List<Course> course = null;
		Category cat=new Category(12,"devloping", "description of category", "abc.jpeg", course);
		Optional<Category> c = Optional.of(cat)  ;
		when(catRepo.findById(12)).thenReturn(c);
		Optional<Category> result = as.getCategoryById(12);
		assertEquals(12, result.get().getCategoryId());	
	}
	@Test
	void testAddCategory() {
		List<Course> course = null;
		Category category = new Category("Testing", "description of category", "abc.jpeg", course);
		when(catRepo.save(category)).thenReturn(category);
		boolean result = as.addCategory(category);
		assertEquals(true, result);
	}
	@Test
	void testDeleteCategory() {
		as.deleteCategory(12);
        verify(catRepo, times(1)).deleteById(12);
	}
	@Test
	void testUpdateCategory() {
		List<Course> course = null;
		Category category = new Category("Testing", "description of category", "abc.jpeg", course);
		category.setCategoryId(12);
		when(catRepo.save(category)).thenReturn(category);
		Optional<Category>c=Optional.of(category);
		boolean status = as.updateCategory(category, c, 12 );
		assertEquals(true, status);
	}
	@Test
	void testGetAllCourse() {
		List<Comment> comment = null;
		Category categoryObj = null;
		List<Course> courseList = new ArrayList<Course>();
		courseList.add(new Course(12,"java", "description of Course", "abc.jpeg", 5000, 3, categoryObj, comment, 0, 0, 0, 0));
		courseList.add(new Course(13,"Testing", "description of Course", "abc.jpeg", 5000, 3, categoryObj, comment, 0, 0, 0, 0));
		courseList.add(new Course(14,"Security", "description of Course", "abc.jpeg", 5000, 3, categoryObj, comment, 0, 0, 0, 0));
		when(courseRepo.findAll()).thenReturn(courseList);
		List<Course> result = as.getAllCourse();
		assertEquals(3, result.size());
	}
	@Test
	void testGetCourseById() {
		Course course = new Course(10,"Spring", "description of spring", "abc.jpeg", 8000, 3);
		Optional<Course> cou = Optional.of(course)  ;
		when(courseRepo.findById(10)).thenReturn(cou);
		Optional<Course> result = as.getCourseById(10);
		assertEquals(10, result.get().getCourseId());	
	}
	@Test
	void testAddCourse() {
		List<Comment> comment = null;
		List<Course> courseList1= new ArrayList<Course>();
		List<Course> courseList = new ArrayList<Course>();
		Category cat = new Category(51,"Testing", "description of category", "abc.jpeg", courseList);
		courseList.add(new Course(12,"java", "description of Course", "abc.jpeg", 5000, 3, cat, comment, 0, 0, 0, 0));
		courseList.add(new Course(13,"Testing", "description of Course", "abc.jpeg", 5000, 3, cat, comment, 0, 0, 0, 0));
		courseList.add(new Course(15,"Security", "description of Course", "abc.jpeg", 5000, 3, cat, comment, 0, 0, 0, 0));
//		System.out.println(categoryObj.getCategoryId());
//		System.out.println(categoryObj.getCourses());
		Course course = new Course("security", "description of Course", "abc.jpeg", 5000, 3);
		when(courseRepo.save(course)).thenReturn(course);
		boolean result = as.addCourse(course, 51);
		System.out.println(result);
		assertEquals(true, result);
	}
	@Test
	void testDeleteCourse() {
		as.deleteCourse(15);
        verify(courseRepo, times(1)).deleteById(15);
	}
	@Test
	void testUpdateCourse() {
		List<Comment> comment = null;
		List<Course> courseList = null;
		Category categoryObj = new Category("Testing", "description of category", "abc.jpeg", courseList);
		Course course = new Course("java", "description of Course", "abc.jpeg", 5000, 3, categoryObj, comment, 0, 0, 0, 0);
		course.setCourseId(12);
		when(courseRepo.save(course)).thenReturn(course);
		Optional<Course> cou = Optional.of(course);
		boolean status = as.updateCourse(course, cou);
		assertEquals(true, status);
	}
//	@Test
//	void testAddVideo() {
//		String course = null;
//		
//		Video video = new Video("Testing", "description of category", "abc.jpeg", course);
//		when(catRepo.save(category)).thenReturn(category);
//		boolean result = as.addCategory(category);
//		assertEquals(true, result);
//	}
	@Test
	void testGetAllVideo() {
		List<Video> video = new ArrayList<Video>();
		Course course = null;
		video.add(new Video(101, "test video 1", "test video desc", "abc.jpeg", course));
		video.add(new Video(102, "test video 2", "test video desc", "abc.jpeg", course));
		video.add(new Video(103, "test video 3", "test video desc", "abc.jpeg", course));
		when(videoRepo.findAll()).thenReturn(video);
		List<Video> result = as.getAllVideo();
		assertEquals(3, result.size());
	}
	@Test
	void testGetVideoById() {
		Course course = null;
		Video video = new Video(101, "test video 1", "test video desc", "abc.jpeg", course);
		Optional<Video> vid = Optional.of(video)  ;
		when(videoRepo.findById(101)).thenReturn(vid);
		Optional<Video> result = as.getVideoById(101);
		assertEquals(101, result.get().getVideoId());	
	}
//	@Test
//	void testUpdateVideo() {
//		List<Comment> comment = null;
//		List<Course> courseList = null;
//		List<EnrolledCourseVideo> ecvideos = null;
//		Category categoryObj = new Category("Testing", "description of category", "abc.jpeg", courseList);
//		Course courseObj = new Course(12,"java", "description of Course", "abc.jpeg", 5000, 3, categoryObj, comment, 0, 0, 0, 0, 0);
//		Video video = new Video("test updated video", "updated video desc", "abc.mp4", ecvideos, courseObj);
//		video.setVideoId(12);
//		when(videoRepo.save(video)).thenReturn(video);
//		boolean status = as.updateVideo(video, 12);
//		assertEquals(true, status);
//	}
	@Test
	void testDeleteVideo() {
		as.deleteVideo(20);
        verify(videoRepo, times(1)).deleteById(20);
	}
	@Test
	void testCountcategory() {
		List<Category> category = new ArrayList<Category>();
		List<Course> course = null;
		category.add(new Category("Testing", "description of category", "abc.jpeg", course));
		category.add(new Category("Cyber Security", "description of category", "abc.jpeg", course));
		category.add(new Category("Heacking", "description of category", "abc.jpeg", course));
		when(catRepo.count()).thenReturn((long) 3);
		long result = as.getCategoryCount();
		assertEquals(3, result);
	}
	@Test
	void testCountCourse() {
		List<Comment> comment = null;
		Category categoryObj = null;
		List<Course> courseList = new ArrayList<Course>();
		courseList.add(new Course(12,"java", "description of Course", "abc.jpeg", 5000, 3, categoryObj, comment, 0, 0, 0, 0));
		courseList.add(new Course(13,"Testing", "description of Course", "abc.jpeg", 5000, 3, categoryObj, comment, 0, 0, 0, 0));
		courseList.add(new Course(14,"Security", "description of Course", "abc.jpeg", 5000, 3, categoryObj, comment, 0, 0, 0, 0));
		when(courseRepo.count()).thenReturn((long)  3);
		long result = as.getCourseCount();
		assertEquals(3, result);
	}
	@Test
	void testCountVideo() {
		List<Video> video = new ArrayList<Video>();
		Course course = null;
		video.add(new Video(101, "test video 1", "test video desc", "abc.jpeg", course));
		video.add(new Video(102, "test video 2", "test video desc", "abc.jpeg", course));
		video.add(new Video(103, "test video 3", "test video desc", "abc.jpeg", course));
		when(videoRepo.count()).thenReturn((long) 3);
		long result = as.getVideoCount();
		assertEquals(3, result);
	}
}