package com.dwellsmart.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.dwellsmart.constants.Endpoints.*;

import com.dwellsmart.dto.request.AuthRequest;
import com.dwellsmart.dto.response.AuthResponse;
import com.dwellsmart.service.impl.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE)
public class AuthenticationController {

  private final AuthenticationService service;
	
	
	@PostMapping(AUTHENTICATE)
	public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest loginRequest) {
		 return ResponseEntity.ok(service.authenticate(loginRequest));
	}
	

//	@PostMapping("/register")
//	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
//		return ResponseEntity.ok(service.register(request));
//	}
//
//	
//
//	@PostMapping("/refresh-token")
//	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		service.refreshToken(request, response);
//	}

}
