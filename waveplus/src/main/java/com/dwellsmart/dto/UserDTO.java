package com.dwellsmart.dto;

import com.dwellsmart.constants.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTO {
	
	
	@NotBlank(message = "Username is mandatory")
	private String username;
	
	@NotBlank(message = "Password is mandatory")
	private String password;
	
	@NotBlank(message = "Email is mandatory")
	private String email;
	
	@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be a valid format (10-15 digits)")
	@NotNull(message = "Phone number is mandatory")
	@JsonProperty("phone_number")
	private String phoneNumber;
	
	@JsonIgnore
	private RoleType roleType;
	
	@JsonIgnore
	private Integer projectId;
	

}
