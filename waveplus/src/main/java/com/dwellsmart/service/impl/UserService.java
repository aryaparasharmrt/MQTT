package com.dwellsmart.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.RoleType;
import com.dwellsmart.dto.ChangePasswordRequestDTO;
import com.dwellsmart.dto.UserDTO;
import com.dwellsmart.entity.Project;
import com.dwellsmart.entity.Role;
import com.dwellsmart.entity.User;
import com.dwellsmart.exception.ResourceNotFoundException;
import com.dwellsmart.repository.RoleRepository;
import com.dwellsmart.repository.UserRepository;
import com.dwellsmart.service.IProjectService;
import com.dwellsmart.service.IUserService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
	public User saveUser(User user) {
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
	            .map(role -> new SimpleGrantedAuthority(role.getRole().name())) 
	            .collect(Collectors.toList());
	    
//	    authorities.forEach(auth -> System.out.println("Authority: " + auth.getAuthority())); // Log authority

	    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

	}

//	@Override
//	public boolean isUsernameExist(String username) {
//		User user = repository.findByUsername(username);
//		return user != null;
//	}
//	
	@Override
	public ResponseEntity<String> changePassword(String username, ChangePasswordRequestDTO changePasswordRequestDTO) {
	    User user = repository.findByUsername(username);
	    if (user != null && passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword())) {
	        String newEncodedPassword = passwordEncoder.encode(changePasswordRequestDTO.getNewPassword());
	        user.setPassword(newEncodedPassword);
	        System.out.print("Password set successfully");
	        repository.save(user);
	        return ResponseEntity.ok("Password changed successfully.");
	    } else {
	        return ResponseEntity.status(401).body("Invalid old password.");
	    }
	}

}
