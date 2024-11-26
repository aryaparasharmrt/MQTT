package com.dwellsmart.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.constants.Endpoints;
import com.dwellsmart.dto.request.ChangePasswordRequestDTO;
import com.dwellsmart.dto.request.CreateUserResquest;
import com.dwellsmart.entity.AccountTransaction;
import com.dwellsmart.entity.User;
import com.dwellsmart.security.JwtUtil;
import com.dwellsmart.service.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(Endpoints.BASE+ Endpoints.USERS)
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
    public ResponseEntity<String> changePassword(Authentication authentication,
                                                 @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        return userService.changePassword(authentication, changePasswordRequestDTO);
        
    }
}
