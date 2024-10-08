package com.dwellsmart.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest{

	@NotBlank(message = "Username is mandatory")
//	@Email(message = "Email should be valid")
	private String username;
	
	@NotBlank(message = "Email is mandatory")
	private String email;

	@NotBlank(message = "Password is mandatory")
//	@Size(min = 6, message = "Password should be at least 6 characters long")
	private String password;

}
