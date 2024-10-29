package com.dwellsmart.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.dwellsmart.dto.ChangePasswordRequestDTO;
import com.dwellsmart.dto.UserDTO;
import com.dwellsmart.entity.User;


public interface IUserService {

//	List<User> findAll();
	
	User saveUser(User user);

	User findByUserName(String username);

	void createNewUser(User user);
    
	ResponseEntity<String> changePassword(String username, ChangePasswordRequestDTO changePasswordRequestDTO);
//	User createNewUser(UserDTO user);
	
//	boolean isUsernameExist(String username);
	
}
