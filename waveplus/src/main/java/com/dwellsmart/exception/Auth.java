package com.dwellsmart.exception;

import org.springframework.security.core.AuthenticationException;

import com.dwellsmart.constants.ErrorCode;

public class Auth extends AuthenticationException {
	private ErrorCode error;

	public Auth(String msg) {
		super(msg);
//		 TODO Auto-generated constructor stub
	}

	public Auth(String msg, ErrorCode error) {
		super(msg);
		this.error = error;
//		 TODO Auto-generated constructor stub
	}

}
