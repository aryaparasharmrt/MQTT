package com.dwellsmart.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.constants.Endpoints;
import com.dwellsmart.constants.RoleType;
import com.dwellsmart.dto.request.CreateUserRequest;
import com.dwellsmart.entity.Role;
import com.dwellsmart.entity.User;
import com.dwellsmart.service.IUserService;

@RestController
@RequestMapping(Endpoints.MANAGER)
public class ManagementController {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private PasswordEncoder encoder;

	@GetMapping
	public String get() {
		return "GET:: management controller";
	}

	@PostMapping("/create")
	public ResponseEntity<String> createManager(@RequestBody CreateUserRequest createManagerRequest) {

		User user = User.builder().username(createManagerRequest.getUsername())
				.password(encoder.encode(createManagerRequest.getPassword())).build();

		Role role = Role.builder().role(RoleType.MANAGER).assignedAt(LocalDateTime.now()).build();

		user.addRole(role); // Add role to the user
		iUserService.createNewUser(user);

		return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");

	}

	@PutMapping
	public String put() {
		return "PUT:: management controller";
	}

	@DeleteMapping
	public String delete() {
		return "DELETE:: management controller";
	}
}
