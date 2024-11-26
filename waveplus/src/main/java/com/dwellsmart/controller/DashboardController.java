package com.dwellsmart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.dto.response.AdminDashboardResponse;
import com.dwellsmart.dto.response.BamUserDashboardResponse;
import com.dwellsmart.dto.response.UserDashboardResponse;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
	
	
	 @GetMapping
	    public ResponseEntity<?> getDashboardData(Authentication authentication) {
		 
		 System.out.println("DashBoard Api call....");
	        // Extract user details from the authentication object (or JWT)
	        String role = authentication.getAuthorities().stream()
	                             .findFirst()
	                             .orElseThrow(() -> new RuntimeException("Role not found"))
	                             .getAuthority();
	        
	        String username = authentication.getName(); 
//	        System.out.println(role);
//	        System.out.println(username);
	        if (role.equals("USER")) {
	            // Prepare User response
	            return ResponseEntity.ok(new UserDashboardResponse(username, "John Doe", "john@example.com", 
	                                "Green Valley Society", "Mr. Alex", "B-102"));
	        } else if (role.equals("ADMIN")) {
	            // Prepare Admin response
	            return ResponseEntity.ok(new AdminDashboardResponse(username, "Jane Smith", "admin@example.com", 
	                                "Green Valley Society"));
	        } else if (role.equals("MANAGER")) {
	            // Prepare BAM user response
	            return ResponseEntity.ok(new BamUserDashboardResponse(username, "Peter Parker", "bam@example.com"));
	        } else {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid role");
	        }
	    }

}
