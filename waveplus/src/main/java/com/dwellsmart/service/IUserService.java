package com.dwellsmart.service;

import java.util.List;

import com.dwellsmart.dto.UserDTO;
import com.dwellsmart.entity.User;


public interface IUserService {

//	List<User> findAll();
	
	User saveUser(User user);

	User findByUserName(String username);

	void createNewUser(User user);

//	User createNewUser(UserDTO user);
	
//	boolean isUsernameExist(String username);
	
}
