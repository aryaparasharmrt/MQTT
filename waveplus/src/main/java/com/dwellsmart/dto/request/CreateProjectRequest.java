package com.dwellsmart.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProjectRequest {

	@NotBlank(message = "Admin name is mandatory")
	private String name;
	
	@NotBlank(message = "Username is mandatory")
	private String username;

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email should be valid")
	private String email;

	@NotBlank(message = "Password is mandatory")
//	@Size(min = 6, message = "Password should be at least 6 characters long")
	private String password;

	@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be a valid format (10-15 digits)")
	private String phoneNumber;

	@NotBlank(message = "Project name is mandatory")
	private String projectName;
}
