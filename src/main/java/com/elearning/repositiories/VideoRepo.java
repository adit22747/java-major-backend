package com.elearning.repositiories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.elearning.entity.Course;
import com.elearning.entity.Video;

public interface VideoRepo extends JpaRepository<Video, Integer> {
	@Query(value = "SELECT v.video_id,v.video_desc,v.video_name,v.video_path,v.sr_no,v.course_id FROM video v,enrolled_courses ec where v.course_id=ec.course_id and ec.user_id=?1 and v.course_id=?2 order by v.sr_no", nativeQuery = true)
	List<Video> getVideo(int uid,int cid);

	List<Video> findAllByCourse(Course course);
	
	
	List<Video> findAllByCourseOrderBySrNoAsc(Course course);
	
	
	
	@Query(value = "SELECT sr_no FROM video where course_id=?1 order by sr_no", nativeQuery = true)
	List<Integer> getVideoSrNo(int cid);
	
	

}
