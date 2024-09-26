package com.dwellsmart.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import com.dwellsmart.dto.DeviceDTO;
import com.dwellsmart.service.IDeviceService;

import static com.dwellsmart.constants.Constants.TOKEN_PREFIX;
import static com.dwellsmart.constants.Endpoints.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	// Define the list of endpoints to exclude from JWT processing
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
//    // JWT validation logic for all other requests
//    String jwt = extractJwtFromRequest(request);
//    if (jwt != null && isValidToken(jwt)) {
//        // Process the JWT and set authentication in the security context
//        setAuthentication(jwt);
//    }
//
//    // Continue the filter chain
//    chain.doFilter(request, response);
//}
//
//private boolean isExcludedPath(String path) {
//    return EXCLUDED_PATHS.contains(path);
//}
//
//private String extractJwtFromRequest(HttpServletRequest request) {
//    // Logic to extract JWT from the request
//    String bearerToken = request.getHeader("Authorization");
//    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//        return bearerToken.substring(7);
//    }
//    return null;
//}
//
//private boolean isValidToken(String token) {
//    // Logic to validate the JWT token
//    return true; // Simplified for example purposes
//}
//
//private void setAuthentication(String jwt) {
//    // Set authentication based on JWT
//    // e.g., SecurityContextHolder.getContext().setAuthentication(authentication);
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

		if (req.getServletPath().contains(PUBLIC)) {
			chain.doFilter(req, res);
			return;
		}
		String header = req.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (token != null) {
			// check some logic in more detail
			String jwtToken = token.replace(TOKEN_PREFIX, "");
			String user = jwtUtil.extractUsername(jwtToken);

			if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = null;
				try {
					userDetails = userDetailsService.loadUserByUsername(user);
				} catch (UsernameNotFoundException e) {
					// Handle exception if user is not found
				}
				
				if (userDetails != null && jwtUtil.validateToken(jwtToken, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}
}
