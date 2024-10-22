package com.dwellsmart.controller;


import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.dwellsmart.constants.Endpoints.*;

import com.dwellsmart.constants.Constants;
import com.dwellsmart.dto.request.AuthRequest;
import com.dwellsmart.dto.request.RefreshTokenOrLogoutRequest;
import com.dwellsmart.dto.response.AuthResponse;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.service.IAuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE + AUTHENTICATE)
public class AuthenticationController {

  private final IAuthenticationService authenticationService;

	// Generate deviceId for web automatically
	public void generateDeviceIdIfWeb(AuthRequest authReq) {
		if (Constants.CLIENT_WEB.equalsIgnoreCase(authReq.getDeviceType())) {
			if (authReq.getDeviceId() == null || authReq.getDeviceId().isEmpty()) {
//			String uuid = UUID.randomUUID().toString();
//			String shortUUID = uuid.replace("-", "").substring(0, 15);  //length we will set from property file 
				authReq.setDeviceId(UUID.randomUUID().toString().replace("-", ""));
			}
		}
		else throw new ApplicationException("Bad Request : Device Type only 'WEB/AND/IOS' Allowed");
	}

	@PostMapping(TOKEN)
	public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest loginRequest) {

		if (Constants.CLIENT_AND.equalsIgnoreCase(loginRequest.getDeviceType())
				|| Constants.CLIENT_IOS.equalsIgnoreCase(loginRequest.getDeviceType())) {
			if (loginRequest.getDeviceId() == null || loginRequest.getDeviceId().isEmpty()) {
				throw new ApplicationException("Device Id is manadenroy for MOB clients");
			}
		} else
			this.generateDeviceIdIfWeb(loginRequest);

		return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
	}

	@PostMapping(REFRESH)
	public ResponseEntity<AuthResponse> refreshAccessToken(@Valid @RequestBody RefreshTokenOrLogoutRequest tokenRequest) {
		return ResponseEntity.ok(authenticationService.refreshToken(tokenRequest));
	}

//	@PostMapping("/register")
//	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
//		return ResponseEntity.ok(service.register(request));
//	}

	@PostMapping(LOGOUT)
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenOrLogoutRequest logoutRequest) {
		
		return  ResponseEntity.ok(authenticationService.logout(logoutRequest));

	}

	
	

}
