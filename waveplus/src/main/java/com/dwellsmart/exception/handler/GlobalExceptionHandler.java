package com.dwellsmart.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dwellsmart.constants.ErrorCode;
import com.dwellsmart.dto.response.ResponseError;
import com.dwellsmart.exception.ApplicationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ResponseError> handleValidationException(ApplicationException ex) {
//		LogMessage.log(LOGGER, " validation exception is -> " + ex.getErrorMessage());
		ResponseError responseError = ResponseError.builder().errorCode(ex.getErrorCode())
				.errorMessage(ex.getErrorMessage()).build();
		
//		LogMessage.log(LOGGER, " paymentResponse is -> " + paymentResponse);
		return ResponseEntity.status(200).body(responseError);
	}
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ResponseError> bodyException(HttpMessageNotReadableException ex) {
//		LogMessage.log(LOGGER, " validation exception is -> " + ex.getErrorMessage());
		ex.printStackTrace();
		ResponseError responseError = ResponseError.builder().errorCode("200")
				.errorMessage(ex.getMessage()).build();
		
//		LogMessage.log(LOGGER, " paymentResponse is -> " + paymentResponse);
		return ResponseEntity.status(200).body(responseError);
	}
//	
//	
//	@ExceptionHandler(AuthenticationException.class)
//	public ResponseEntity<?> handleValidationException1(AuthenticationException ex) {
////		LogMessage.log(LOGGER, " validation exception is -> " + ex.getErrorMessage());
//		ResponseError responseError = ResponseError.builder().errorCode(ErrorCodeEnum.SIGNATURE_NOT_FOUND.getErrorMessage())
//				.errorMessage(ex.getMessage()).build();
////		LogMessage.log(LOGGER, " paymentResponse is -> " + paymentResponse);
//		return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<?> handleValidationException1(MethodArgumentNotValidException ex) {
////		LogMessage.log(LOGGER, " validation exception is -> " + ex.getErrorMessage());
//		ex.printStackTrace();
//		String detailMessageCode = ex.getDetailMessageCode();
//		String message = ex.getMessage();
//		System.out.println(detailMessageCode);
//				System.out.println(message);
//				String typeMessageCode = ex.getTypeMessageCode();
//				System.out.println(typeMessageCode);
//				System.out.println(ex);
//		ResponseError responseError = ResponseError.builder().errorCode(ErrorCodeEnum.SIGNATURE_NOT_FOUND.getErrorMessage())
//				.errorMessage(ex.getMessage()).build();
////		LogMessage.log(LOGGER, " paymentResponse is -> " + paymentResponse);
//		return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//	
	

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Extracting validation errors
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseError> handleGenericException(Exception ex) {
//		LogMessage.log(LOGGER, " generic exception message is -> " + ex.getMessage());
//		LogMessage.logException(LOGGER, ex);
		ex.printStackTrace();
		ResponseError responseError = ResponseError.builder().errorCode(ErrorCode.GENERIC_EXCEPTION.getErrorCode())
				.errorMessage(ErrorCode.GENERIC_EXCEPTION.getErrorMessage()).build();
//		LogMessage.log(LOGGER, " paymentResponse is -> " + paymentResponse);
		return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
}
