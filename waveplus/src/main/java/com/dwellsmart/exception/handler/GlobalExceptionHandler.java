package com.dwellsmart.exception.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dwellsmart.constants.ErrorCodeEnum;
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
		return ResponseEntity.status(ex.getHttpStatus()).body(responseError);
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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseError> handleGenericException(Exception ex) {
//		LogMessage.log(LOGGER, " generic exception message is -> " + ex.getMessage());
//		LogMessage.logException(LOGGER, ex);
		ex.printStackTrace();
		ResponseError responseError = ResponseError.builder().errorCode(ErrorCodeEnum.GENERIC_EXCEPTION.getErrorCode())
				.errorMessage(ErrorCodeEnum.GENERIC_EXCEPTION.getErrorMessage()).build();
//		LogMessage.log(LOGGER, " paymentResponse is -> " + paymentResponse);
		return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
}
