package com.example.repositiories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Category;

public interface Categoryrepo extends JpaRepository<Category, Integer> {

	public Category findByCategoryName(String name);

	public Object save(Optional<Category> cati);
	
	@Query(value ="select c.category_name , c.category_id from category c" , nativeQuery = true) 
	public List<Object> findCategoryName();
}
