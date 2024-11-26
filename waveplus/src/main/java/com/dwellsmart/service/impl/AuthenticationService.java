package com.dwellsmart.service.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.ErrorCode;
import com.dwellsmart.dto.DeviceDTO;
import com.dwellsmart.dto.request.AuthRequest;
import com.dwellsmart.dto.request.RefreshTokenOrLogoutRequest;
import com.dwellsmart.dto.response.AuthResponse;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.security.JwtUtil;
import com.dwellsmart.service.IAuthenticationService;
import com.dwellsmart.service.IDeviceService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
//	private final UserRepository userRepository;
//	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final AuthenticationManager authenticationManager;

	private final IDeviceService deviceService;
	private final UserDetailsService userDetailsService;

	@Autowired
	private HttpServletRequest request;

	public String getCustomField() {
		// Fetch custom field from the request attribute
		return (String) request.getAttribute("customField");
	}

//	public AuthenticationResponse register(RegisterRequest request) {
//		var user = User.builder().firstname(request.getFirstname()).lastname(request.getLastname())
//				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
//				.role(request.getRole()).build();
//		var savedUser = repository.save(user);
//		var jwtToken = jwtService.generateToken(user);
//		var refreshToken = jwtService.generateRefreshToken(user);
//		saveUserToken(savedUser, jwtToken);
//		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
//	}

	@Override
	public AuthResponse authenticate(AuthRequest loginRequest) {
		try {
			// Authenticate the user
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			// Set the authentication in the security context
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Retrieve user details from the authentication object
			var userDetails = (UserDetails) authentication.getPrincipal();

			// Generate JWT token using user details
			final var jwtToken = jwtUtil.generateToken(userDetails, loginRequest.getDeviceId());
			final String secureRefreshToken = jwtUtil.generateSecureRefreshToken();

			DeviceDTO deviceDTO = DeviceDTO.builder().deviceId(loginRequest.getDeviceId())
					.deviceType(loginRequest.getDeviceType()).revoked(false).tokenCreatedAt(LocalDateTime.now())
//		                .OS(loginRequest.getOs())
//		                .version(loginRequest.getVersion())
					.refreshToken(secureRefreshToken).loginDate(LocalDateTime.now())
					.username(loginRequest.getUsername()).build();

			deviceService.manageDevice(deviceDTO);

			return AuthResponse.builder().accessToken(jwtToken).refreshToken(secureRefreshToken)
					.deviceId(loginRequest.getDeviceId()).build();

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.AUTHENTICATION_FAILED); 
		}

	}

	@Override
	public AuthResponse refreshToken(RefreshTokenOrLogoutRequest tokenRequest) {

		DeviceDTO deviceDTO = deviceService
				.getDeviceByDeviceIdAndRefToken(tokenRequest.getDeviceId(), tokenRequest.getRefreshToken()).orElseThrow(
						() -> new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN_OR_DEVICE_ID));

		var userDetails = userDetailsService.loadUserByUsername(deviceDTO.getUsername());

		final String newAccessToken = jwtUtil.generateToken(userDetails,tokenRequest.getDeviceId());

		final String secureRefreshToken = jwtUtil.generateSecureRefreshToken();

		deviceDTO.setRefreshToken(secureRefreshToken);
		deviceDTO.setTokenCreatedAt(LocalDateTime.now());

//
		deviceService.manageDevice(deviceDTO);

		return AuthResponse.builder().refreshToken(secureRefreshToken).deviceId(tokenRequest.getDeviceId())
				.accessToken(newAccessToken) // Assign the newly generated access token here
				.build();
	}

	@Override
	public String logout(RefreshTokenOrLogoutRequest logoutRequest) {
		
		DeviceDTO deviceDTO = deviceService.getDeviceByDeviceIdAndRefToken(logoutRequest.getDeviceId(),logoutRequest.getRefreshToken())
			    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN_OR_DEVICE_ID));
		
		 deviceDTO.setRevoked(true);
		 deviceService.manageDevice(deviceDTO);
		return "Logout Successful";
	}

//
//	private void revokeAllUserTokens(User user) {
//		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//		if (validUserTokens.isEmpty())
//			return;
//		validUserTokens.forEach(token -> {
//			token.setExpired(true);
//			token.setRevoked(true);
//		});
//		tokenRepository.saveAll(validUserTokens);
//	}

}
