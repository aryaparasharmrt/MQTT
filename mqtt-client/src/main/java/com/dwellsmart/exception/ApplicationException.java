package com.dwellsmart.exception;


import com.dwellsmart.constants.ErrorCode;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = -2171272011475853092L;

	private final ErrorCode errorCode;

//	public ApplicationException(String errorMessage, Throwable th) {
//		super(errorMessage, th);
//		this.errorMessage = errorMessage;
//	}

//	public String getErrorMessage() {
//		return errorCode.getErrorMessage();
//	}

	public String getCode() {
		return errorCode.getErrorCode();
	}

	public ErrorCode getErrorCodeEnum() {
		return errorCode;
	}

	public ApplicationException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
	
	public ApplicationException(ErrorCode errorCode, String message) {
		super(errorCode.getErrorMessage()+ message);
		this.errorCode = errorCode;
	}
	
	public ApplicationException(String message) {
		super(message);
		this.errorCode = ErrorCode.GENERIC_EXCEPTION;
	}

}
