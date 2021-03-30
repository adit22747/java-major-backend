package com.example.repositiories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Certificate;
import com.example.entity.Course;
import com.example.entity.User;

public interface CertiRepo extends JpaRepository<Certificate, Integer>{
	public Certificate findByUserAndCourse(User u,Course c);
	
	@Query(value="select certi_path from certificate where user_id=?1 order by course_id",nativeQuery = true)
    public List<String> getCertipaths(int userid);
}