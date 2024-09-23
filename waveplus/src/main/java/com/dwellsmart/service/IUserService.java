package com.dwellsmart.service;

import java.util.List;

import com.dwellsmart.entity.User;


public interface IUserService {

//	List<User> findAll();

	User findByUserName(String username);

	User createNewUser(User user);
	
//	boolean isUsernameExist(String username);
	
}
