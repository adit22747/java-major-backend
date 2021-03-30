package com.elearning.repositiories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.elearning.entity.Course;
import com.elearning.entity.Like;
import com.elearning.entity.User;

public interface LikeRepo extends JpaRepository<Like, Integer>{
	public Like findByCourseAndUser(Course c,User u);
//	@Query("insert into like(user_id values(")
//	public List<Player> fBName(String name);
}