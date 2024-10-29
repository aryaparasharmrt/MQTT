package com.dwellsmart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.constants.Endpoints;
import com.dwellsmart.dto.ChangePasswordRequestDTO;
import com.dwellsmart.dto.request.AuthRequest;
import com.dwellsmart.dto.request.CreateUserResquest;
import com.dwellsmart.dto.response.AuthResponse;
import com.dwellsmart.entity.User;
import com.dwellsmart.security.JwtUtil;
import com.dwellsmart.service.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;





//	@GetMapping("/find/all")
//	public FindAllUsersResponse findAll() {
//		List<User> userList = userService.findAll();
//		
//		FindAllUsersResponse response = new FindAllUsersResponse();
//		response.setUserList(userList);
//		return response;
//	}

//	!Pattern.matches("[0-9]+", request.getTcno())
	

	@PostMapping(Endpoints.AUTHENTICATE)
	public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest loginRequest) {

		try {
			// Authenticate the user
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			// Set the authentication in the security context
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Retrieve user details from the authentication object
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			// Generate JWT token using user details
			final String jwt = jwtUtil.generateToken(userDetails);

			AuthResponse auth = AuthResponse.builder().accessToken(jwt).refreshToken(null).build();

			return ResponseEntity.status(HttpStatus.OK).body(auth);

		} catch (Exception e) {
			throw new RuntimeException("Authentication failed", e);
		}

	}

	@PostMapping("/create")
	public CreateUserResquest createUser(@Valid @RequestBody CreateUserResquest request) {

//		if (request.getUsername() == null || request.getUsername().equals("")) {
//			throw new BadRequestException(Constants.MESSAGE_INVALIDUSERNAME);
//		}
//		
//		if (request.getPassword() == null || request.getPassword().equals("")) {
//			throw new BadRequestException(Constants.MESSAGE_INVALIDPASSWORD);
//		}

		

//		boolean isUsernameExist = userService.isUsernameExist(request.getUsername());
//		if (isUsernameExist) {
//			throw new BadCredentialsException(Constants.MESSAGE_SAMEUSERNAMEEXIST);
//		}

		User user = new User();
		user.setPassword(request.getPassword());
		user.setUsername(request.getUsername());
	
//		user.setRole("USER");
//		UserDTO userRes = userService.createNewUser(user);

		CreateUserResquest response = new CreateUserResquest();
//		response.setUsername(userRes.getUsername());
		return response;
	}
//	
//	@PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user) {
//        if (userRepository.findByUsername(user.getUsername()) != null) {
//            return ResponseEntity.badRequest().body("Username already exists");
//        }
//
//        // You may want to hash the password before saving to the database
//         user.setPassword(encoder.encode(user.getPassword()));
//
//        userRepository.save(user);
//        return ResponseEntity.ok("Registration successful");
//    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(UsernamePasswordAuthenticationToken token,
                                                 @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        // Extract username from the token
//        String username = jwtUtil.extractUsername(token.g(7)); // Remove "Bearer " prefix
    	String username = ((UserDetails)token.getPrincipal()).getUsername();
    	
    	System.out.println(((UserDetails)token.getPrincipal()).getPassword());
        return userService.changePassword(username, changePasswordRequestDTO);
        
    }
}
