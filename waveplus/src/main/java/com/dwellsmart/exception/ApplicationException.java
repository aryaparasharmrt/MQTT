package com.dwellsmart.exception;

import org.springframework.http.HttpStatus;

import com.dwellsmart.constants.ErrorCode;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = -2171272011475853092L;

	private final ErrorCode errorCode;

//	public ApplicationException(String errorCode, String errorMessage, HttpStatus httpStatus) {
//		super();
//		this.httpStatus = httpStatus;
//		this.errorCode = errorCode;
//		this.errorMessage = errorMessage;
//	}

//	public ApplicationException(String errorMessage) {
//		super(errorMessage);
//		this.errorMessage = errorMessage;
//		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//		
//		
////		set error code and http stats
//	}

//	public ApplicationException(String errorMessage, Throwable th) {
//		super(errorMessage, th);
//		this.errorMessage = errorMessage;
//	}

	public HttpStatus getHttpStatus() {
		return errorCode.getHttpStatus();
	}

	public String getErrorMessage() {
		return errorCode.getErrorMessage();
	}

	public String getErrorCode() {
		return errorCode.getErrorCode();
	}

	public ErrorCode getErrorCodeEnum() {
		return errorCode;
	}

	public ApplicationException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}

}
