package com.lhs.documentTextExtraction.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.lhs.documentTextExtraction.entity.UserMast;



@EnableJpaRepositories
@Repository
public interface UserMastRepo extends JpaRepository<UserMast,String> {

	

	
	 @Query(value="Select * from  user_mast where user_code =:login_id and password =:login_pw", nativeQuery = true)
		List<UserMast> validatedUser(String login_id,String login_pw);


	
}