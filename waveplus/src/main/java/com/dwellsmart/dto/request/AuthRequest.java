package com.dwellsmart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

	@NotBlank(message = "Username is mandatory")
	private String username;
	
	@NotBlank(message = "Password is mandatory")
	private String password;
	
	@NotBlank(message = "Device Type is mandatory")
	@JsonProperty("device_type")
	private String deviceType;

	@JsonProperty("device_id")
	private String deviceId;

}
