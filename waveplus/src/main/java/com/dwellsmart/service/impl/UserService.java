package com.dwellsmart.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dwellsmart.entity.User;
import com.dwellsmart.exception.ResourceNotFoundException;
import com.dwellsmart.repository.RoleRepository;
import com.dwellsmart.repository.UserRepository;
import com.dwellsmart.service.IUserService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {

	private final UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
//	@Override
//	public List<User> findAll() {
//		return repository.findAll();
//	}

	@Transactional
	@Override
	public User createNewUser(User user) {
		
		return repository.save(user);
	}

	@Override
	public User findByUserName(String username) {
		
		return  repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		
		User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Convert all user roles to GrantedAuthority
	    List<GrantedAuthority> authorities = user.getRoles().stream()
	            .map(role -> new SimpleGrantedAuthority(role.getRole().name())) // Example roles: "ROLE_ADMIN", "ROLE_USER"
	            .collect(Collectors.toList());

	    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

	}

//	@Override
//	public boolean isUsernameExist(String username) {
//		User user = repository.findByUsername(username);
//		return user != null;
//	}
	

}
