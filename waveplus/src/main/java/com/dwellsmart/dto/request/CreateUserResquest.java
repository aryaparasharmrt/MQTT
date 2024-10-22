package com.dwellsmart.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResquest{

	@NotBlank(message = "Username is mandatory")
	private String username;
	
	@NotBlank(message = "Email is mandatory")
//	@Email(message = "Email should be valid")
	private String email;

	@NotBlank(message = "Password is mandatory")
//	@Size(min = 6, message = "Password should be at least 6 characters long")
	private String password;

}
