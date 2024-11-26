package com.dwellsmart.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.dwellsmart.dto.response.ResponseError;
import com.dwellsmart.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UnAuthorizedHandler implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(UnAuthorizedHandler.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
//    logger.error("Unauthorized error: {}", authException.getMessage());
//    
////    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + authException.getMessage());
//
//    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//    
//    System.out.println(authException);
//
//    final Map<String, Object> body = new HashMap<>();
//    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
//    body.put("error", "Unauthorized");
//    body.put("message", authException.getMessage());
//    body.put("path", request.getServletPath());
//
//    final ObjectMapper mapper = new ObjectMapper();
//    mapper.writeValue(response.getOutputStream(), body);
	  
//	    logger.error("Unauthorized error: {}", authException.getMessage());
//
//	    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	    
//	    // Create your custom error response
//	    ResponseError responseError = new ResponseError();
//	    responseError.setErrorCode("401"); // You can define specific error codes as needed
//	    responseError.setErrorMessage(authException.getMessage());
//
//	    // Use ObjectMapper to write the response
//	    final ObjectMapper mapper = new ObjectMapper();
//	    mapper.writeValue(response.getOutputStream(), responseError);
	  
	    logger.error("Unauthorized error: {}", authException.getMessage());
	    System.out.println("thii...");
	    

	    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    
	    
	    System.out.println(authException.toString());
	    System.out.println("thih..."+authException.getCause());
//	    System.out.println(AuthenticationException);

	    // Check if the authException is an instance of ApplicationException
	    if (authException.getCause() instanceof ApplicationException) {
	        ApplicationException appEx = (ApplicationException) authException.getCause();
	        // Create your custom error response based on ApplicationException
	        ResponseError responseError = new ResponseError();
	        responseError.setErrorCode(appEx.getErrorCode()); // Assuming ErrorCode has a method getCode()
	        responseError.setErrorMessage(appEx.getMessage());

	        // Write the custom error response to the output
	        final ObjectMapper mapper = new ObjectMapper();
	        mapper.writeValue(response.getOutputStream(), responseError);
	    } else {
	        // Handle other authentication exceptions (if any)
	        ResponseError responseError = new ResponseError();
	        responseError.setErrorCode("401"); // Default error code
	        responseError.setErrorMessage(authException.getMessage());

	        // Write the generic error response to the output
	        final ObjectMapper mapper = new ObjectMapper();
	        mapper.writeValue(response.getOutputStream(), responseError);
	    }
  }

}
