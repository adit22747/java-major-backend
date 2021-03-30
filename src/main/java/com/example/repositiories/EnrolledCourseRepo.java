package com.example.repositiories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Comment;
import com.example.entity.Course;
import com.example.entity.EnrolledCourses;
import com.example.entity.Like;
import com.example.entity.User;

public interface EnrolledCourseRepo extends JpaRepository<EnrolledCourses, Integer> {
	public List<EnrolledCourses> findAllByUserAndCourse(User u,Course c);
	public EnrolledCourses findByUserAndCourse(User user, Course course);
	public List<EnrolledCourses> findAllByUser(User u);
	public List<EnrolledCourses> findByCourseAndUser(Course course, User user);
	@Query(value = "SELECT c.course_name,c.course_desc FROM `java-major`.enrolled_courses e, `java-major`.course c where e.user_id=?1 and end_date is null and e.course_id=c.course_id", nativeQuery = true)
	public List<Course> enrolledcourse(int id);
	
	

	@Query(value="select start_date from enrolled_courses where user_id=?1 and end_date is null order by course_id",nativeQuery = true)
    public List<Date> getEnrollmentCourseDates(int userid);
	
	@Query(value="select end_date from enrolled_courses where user_id=?1 and end_date is not null order by course_id",nativeQuery = true)
    public List<Date> getFinishedCourseDates(int userid);
	
	@Query(value="select ecourse_id from enrolled_courses where user_id=?1 and end_date is null order by course_id",nativeQuery = true)
    public List<Integer> getEnrollmentCourseIds(int userid);
	public List<EnrolledCourses> findAllByCourse(Course course);

	
	
	

 }
