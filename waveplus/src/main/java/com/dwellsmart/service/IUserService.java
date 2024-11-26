package com.dwellsmart.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.dwellsmart.dto.UserDTO;
import com.dwellsmart.dto.request.ChangePasswordRequestDTO;
import com.dwellsmart.entity.User;


public interface IUserService {

//	List<User> findAll();
	
	User saveUser(User user);

	User findByUserName(String username);

	void createNewUser(User user);
    
	ResponseEntity<String> changePassword(Authentication authentication, ChangePasswordRequestDTO changePasswordRequestDTO);
//	User createNewUser(UserDTO user);
	
//	boolean isUsernameExist(String username);
	
}
