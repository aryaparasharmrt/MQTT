package com.dwellsmart.service;

import com.dwellsmart.dto.request.AuthRequest;
import com.dwellsmart.dto.response.AuthResponse;

public interface IAuthenticationService {
	
	AuthResponse authenticate(AuthRequest loginRequest);
	

}
