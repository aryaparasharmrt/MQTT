package com.dwellsmart.exception;

import com.dwellsmart.constants.ErrorCode;

public class ModbusConnectionException extends RuntimeException {

	private static final long serialVersionUID = -6566316610483296103L;
	
	
	private final ErrorCode errorCode;

//	public CautionException(String errorMessage, Throwable th) {
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

	public ModbusConnectionException(ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
	
	public ModbusConnectionException(ErrorCode errorCode, String message) {
		super(errorCode.getErrorMessage()+ message);
		this.errorCode = errorCode;
	}
	
	public ModbusConnectionException(String message) {
		super(message);
		this.errorCode = ErrorCode.GENERIC_EXCEPTION;
	}
	
	public ModbusConnectionException(String message, Throwable cause) {
		super(message, cause);
        this.errorCode = ErrorCode.GENERIC_EXCEPTION;
    }

}
