package com.elearning.repositiories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearning.entity.Profile;
import com.elearning.entity.User;

public interface ProfileRepo extends JpaRepository<Profile, Integer>{
	public Profile findByUser(User u);

	
}
