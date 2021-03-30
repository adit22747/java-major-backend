package com.elearning.repositiories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.elearning.entity.Course;
import com.elearning.entity.EnrolledCourses;
import com.elearning.entity.Feedback;
import com.elearning.entity.User;

public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {
	@Query(value = "SELECT * FROM feedback where course_id=?1", nativeQuery = true)
	public List<Feedback> fetchfeedback(int id);
	public List<Feedback> findAllByUserAndCourse(User u,Course c);
	public List<Feedback> findAllByCourse(Course c);
}
