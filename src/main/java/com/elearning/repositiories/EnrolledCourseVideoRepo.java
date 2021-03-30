package com.elearning.repositiories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.elearning.entity.EnrolledCourseVideo;
import com.elearning.entity.EnrolledCourses;
import com.elearning.entity.Video;


@Repository
public interface EnrolledCourseVideoRepo extends JpaRepository<EnrolledCourseVideo, Integer>{
	public List<EnrolledCourseVideo> findAllByEc(EnrolledCourses ec);
	public boolean deleteAllByEc(EnrolledCourses ec);
	
	@Query(value="Select COUNT(completed)  as completed FROM enrolled_course_video where ecourse_id=?1 and completed is true;",nativeQuery = true)
    public int noOfCompletedVideo(int ecid);
	
	@Query(value="Select completed  FROM enrolled_course_video where ecourse_id=?1 ",nativeQuery = true)
    public List<Boolean> videoStatus(int ecid);
	
	
	@Modifying
    @Transactional 
	@Query(value="delete from  enrolled_course_video where ecourse_id=?1 ",nativeQuery = true)
    public void DeleteEcv(int ecid);
}