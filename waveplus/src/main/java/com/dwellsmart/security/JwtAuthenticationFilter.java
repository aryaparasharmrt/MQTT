package com.dwellsmart.security;

import static com.dwellsmart.constants.Constants.TOKEN_PREFIX;
import static com.dwellsmart.constants.Endpoints.AUTHENTICATE;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.dwellsmart.constants.ErrorCode;
import com.dwellsmart.dto.DeviceDTO;
import com.dwellsmart.dto.response.ResponseError;
import com.dwellsmart.exception.Auth;
import com.dwellsmart.service.IDeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

//    private static final List<String> EXCLUDED_PATHS = List.of("/authenticate", "/register");
	// Get the request URI
//    String requestPath = request.getRequestURI();
//
//    // If the request path is in the excluded list, skip JWT validation
//    if (isExcludedPath(requestPath)) {
//        // Simply pass the request down the filter chain, no JWT validation
//        chain.doFilter(request, response);
//        return;
//    }
//
//private boolean isExcludedPath(String path) {
//    return EXCLUDED_PATHS.contains(path);
//}

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private IDeviceService deviceService;

	public JwtAuthenticationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res,
			@NonNull FilterChain chain) throws ServletException, IOException {

		if (isAuthenticationRequest(req)) {
			chain.doFilter(req, res);
			return;
		}

		String header = req.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

//		try {
			UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			else {  //Authentication Failed.
				 SecurityContextHolder.clearContext();
				throw new Auth("Unauth",ErrorCode.AUTHENTICATION_FAILED);
			}
			
//		}catch(ApplicationException ex){
////			throw new AuthenticationServiceException("Authentication Failed! "+ ex);
//		}

//		try {
			
			chain.doFilter(req, res);
//		}
//		catch(Exception e) {
//			System.out.println("hii...");
//			System.out.println(e);
//			
//			 res.setContentType(MediaType.APPLICATION_JSON_VALUE);
//			    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			    
//			    
////			    System.out.println(authException.toString());
////			    System.out.println("thih..."+authException.getCause());
////			    System.out.println(AuthenticationException);
//
//			    // Check if the authException is an instance of ApplicationException
////			    if (authException.getCause() instanceof ApplicationException) {
////			        ApplicationException appEx = (ApplicationException) authException.getCause();
//			        // Create your custom error response based on ApplicationException
//			        ResponseError responseError = new ResponseError();
//			        responseError.setErrorCode(ErrorCode.AUTHENTICATION_FAILED.getErrorCode()); // Assuming ErrorCode has a method getCode()
//			        responseError.setErrorMessage(ErrorCode.AUTHENTICATION_FAILED.getErrorMessage());
//
//			        // Write the custom error response to the output
//			        final ObjectMapper mapper = new ObjectMapper();
//			        mapper.writeValue(res.getOutputStream(), responseError);
////			    } else {
//			        // Handle other authentication exceptions (if any)
////			        ResponseError responseError = new ResponseError();
////			        responseError.setErrorCode("401"); // Default error code
////			        responseError.setErrorMessage(authException.getMessage());
////
////			        // Write the generic error response to the output
////			        final ObjectMapper mapper = new ObjectMapper();
////			        mapper.writeValue(res.getOutputStream(), responseError);
////			    }
//
//		}
//		

	}

	private boolean isAuthenticationRequest(HttpServletRequest request) {
		return request.getServletPath().contains(AUTHENTICATE);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		String jwtToken = token.replace(TOKEN_PREFIX, ""); // Token prefix already checked

		if (jwtToken != null) {
			String username = jwtUtil.extractUsername(jwtToken);
			String deviceId = jwtUtil.extractDeviceId(jwtToken);

			if (!isValidDevice(deviceId)) {
				return null; // Device revoked, stop processing
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = null;
				try {
					userDetails = userDetailsService.loadUserByUsername(username);
				} catch (UsernameNotFoundException e) {
					// Log the exception if user is not found
					return null;
				}

				if (userDetails != null && jwtUtil.validateToken(jwtToken, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					return usernamePasswordAuthenticationToken;
				}
			}
		}

		return null;

	}

	private boolean isValidDevice(String deviceId) {
		Optional<DeviceDTO> deviceOpt = deviceService.getDeviceByDeviceId(deviceId);

		if (deviceOpt.isPresent() && deviceOpt.get().isRevoked()) {
			System.out.println("Device is Revoked...");
			return false;
		}
		return true;
	}

}
