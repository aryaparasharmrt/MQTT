package com.dwellsmart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

	@NotBlank
	private String username;
	@NotBlank
	private String password;
	
	@NotBlank
	@JsonProperty("device_type")
	private String deviceType;

	@JsonProperty("device_id")
	private String deviceId;

}
