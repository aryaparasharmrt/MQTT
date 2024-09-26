package com.dwellsmart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenOrLogoutRequest {

	@NotBlank(message = "Device ID is mandatory")
	@JsonProperty("device_id")
	private String deviceId;
	
	@NotBlank(message = "Refresh Token is mandatory")
	@JsonProperty("refresh_token")
	private String refreshToken;

}
