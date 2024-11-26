package com.dwellsmart.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.ErrorCode;
import com.dwellsmart.dto.request.ChangePasswordRequestDTO;
import com.dwellsmart.entity.Device;
import com.dwellsmart.entity.User;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.repository.UserRepository;
import com.dwellsmart.service.IUserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {

	private final UserRepository repository;
	
	@Autowired
	@Lazy
	PasswordEncoder passwordEncoder;
	
	
//	@Override
//	public List<User> findAll() {
//		return repository.findAll();
//	}
	
	public Optional<User> findById(Long userId) {
		return repository.findById(userId);
	}

	@Transactional
	@Override
	public void createNewUser(User user) {
		
//		Optional<Project> projectById = iProjectService.getProjectById(userDTO.getProjectId());
//		
//		User user = User.builder().email(userDTO.getEmail()).password(encoder.encode(userDTO.getPassword()))
//				.username(userDTO.getUsername()).build();
//		user.addRole(
//				Role.builder().role(userDTO.getRoleType()).assignedAt(LocalDateTime.now()).project(projectById.get()).build());
		
		 repository.save(user);
	}
	
	@Override
	@Transactional
	public User saveUser(User user) {
		return repository.save(user);
	}

	@Override
	public User findByUserName(String username) {
		System.out.println("This is Getting New User Reqeuest ::");
		
		User u = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		return  u;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		
		User user = repository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

		// Convert all user roles to GrantedAuthority
	    List<GrantedAuthority> authorities = user.getRoles().stream()
	            .map(role -> new SimpleGrantedAuthority(role.getRole().name())) 
	            .collect(Collectors.toList());
	    
//	    authorities.forEach(auth -> System.out.println("Authority: " + auth.getAuthority())); // Log authority

	    System.out.println("Load User :: "+user);
	    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

	}

//	@Override
//	public boolean isUsernameExist(String username) {
//		User user = repository.findByUsername(username);
//		return user != null;
//	}
//	
	@Override
	@Transactional
	public ResponseEntity<String> changePassword(Authentication authentication, ChangePasswordRequestDTO changePasswordRequestDTO) {
	    try {
	        User user = (User) authentication.getPrincipal();
	        
	        if (user == null) {
	            return ResponseEntity.status(404).body("User not found.");
	        }
	        
	        if (passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword())) {
	            String newEncodedPassword = passwordEncoder.encode(changePasswordRequestDTO.getNewPassword());
	            user.setPassword(newEncodedPassword);
	            
//	            Set<Device> devices = user.getDevices();
//	            user.setDevices(null); // Detach `Device` collection temporarily
	            // Save without explicitly flushing unless necessary
//	            repository.save(user);
	            saveUser(user);
//	            user.setDevices(devices); // Restore if needed
	            System.out.println("Password Changed");
	            return ResponseEntity.ok("Password changed successfully.");
	        } else {
	            return ResponseEntity.status(401).body("Invalid old password.");
	        }
	    } catch (Exception e) {
	        // Log exception properly (use a logger in production instead of printStackTrace)
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("An error occurred while changing the password.");
	    }
	}


}
