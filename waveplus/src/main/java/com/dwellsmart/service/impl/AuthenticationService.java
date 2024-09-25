package com.dwellsmart.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dwellsmart.dto.DeviceDTO;
import com.dwellsmart.dto.request.AuthRequest;
import com.dwellsmart.dto.response.AuthResponse;
import com.dwellsmart.entity.User;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.security.JwtUtil;
import com.dwellsmart.service.IAuthenticationService;
import com.dwellsmart.service.IDeviceService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
//	private final UserRepository userRepository;
//	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final AuthenticationManager authenticationManager;

	private final IDeviceService deviceService;

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
			
			List<GrantedAuthority> filteredAuthorities = authentication.getAuthorities().stream()
			        .collect(Collectors.toList());

			    // Create a new Authentication token with only the active role
//			    Authentication newAuth = new UsernamePasswordAuthenticationToken(
//			        authentication.getPrincipal(),
//			        authentication.getCredentials(),
//			        filteredAuthorities
//			    );

			// Generate JWT token using user details
			final var jwtToken = jwtUtil.generateToken(userDetails);
			String secureRefreshToken = jwtUtil.generateSecureRefreshToken();

			DeviceDTO deviceDTO = DeviceDTO.builder().deviceId(loginRequest.getDeviceId())
					.deviceType(loginRequest.getDeviceType())
					.revoked(false)
					.expired(false)
					.tokenCreatedAt(LocalDateTime.now())
//		                .OS(loginRequest.getOs())
//		                .version(loginRequest.getVersion())
					.refreshToken(secureRefreshToken).username(loginRequest.getUsername()).build();

			deviceService.manageDevice(deviceDTO);

//		var refreshToken = jwtUtil.generateRefreshToken(user);
//		revokeAllUserTokens(user);
//		saveUserToken(user, jwtToken);
			return AuthResponse.builder().accessToken(jwtToken).refreshToken(secureRefreshToken)
					.deviceId(loginRequest.getDeviceId()).build();

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Authentication failed", e);  //custom application exception
		}

	}


//	private void saveUserToken(User user, String jwtToken) {
//		var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false)
//				.build();
//		tokenRepository.save(token);
//	}
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

//	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//		final String refreshToken;
//		final String userEmail;
//		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//			return;
//		}
//		refreshToken = authHeader.substring(7);
//		userEmail = jwtUtil.extractUsername(refreshToken);
//		if (userEmail != null) {
//			var user = this.repository.findByEmail(userEmail).orElseThrow();
//			if (jwtUtil.isTokenValid(refreshToken, user)) {
//				var accessToken = jwtUtil.generateToken(user);
//				revokeAllUserTokens(user);
//				saveUserToken(user, accessToken);
//				var authResponse = AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
//						.build();
//				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//			}
//		}
//	}
}
