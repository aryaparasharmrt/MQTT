package com.dwellsmart.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = -2171272011475853092L;
	private HttpStatus httpStatus;
	private String errorCode;
	private String errorMessage;

	public ApplicationException(HttpStatus httpStatus, String errorCode, String errorMessage) {
		super();
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public ApplicationException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		
		
//		set error code and http stats
	}
	
	public ApplicationException(String errorMessage , Throwable th) {
		super(errorMessage,th);
		this.errorMessage = errorMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
