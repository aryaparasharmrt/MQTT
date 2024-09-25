package com.dwellsmart.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class DeviceDTO {

	@NotEmpty(message = "Device ID is mandatory")
	private String deviceId;

	private String deviceType; // Optional unless it's mobile

//	private String OS; // Mandatory for mobile
//
//	private String version; // Mandatory for mobile

	private String username;

	private boolean revoked;

	private LocalDateTime tokenCreatedAt;
	
	private boolean expired;

	private String refreshToken;

}
