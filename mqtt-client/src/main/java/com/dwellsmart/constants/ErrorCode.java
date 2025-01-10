package com.dwellsmart.constants;

import lombok.Getter;

public enum ErrorCode {
	
	GENERIC_EXCEPTION("ERR000", "Something went wrong, please try later"),
	INVALID_JSON("ERR001", "Invalid JSON "),
    MISSING_FIELD("ERR002", "Validation failed: Missing required field"),
    INVALID_IP_FORMAT("ERR003", "Invalid IP address format"),
    EMPTY_METERS("ERR004", "Meters list cannot be empty"),
    INVALID_METER_ID("ERR005", "Meter ID must be between 0 and 255");
	
	@Getter
	private final String errorCode;
	@Getter
	private final String errorMessage;
	
	private ErrorCode(String errorCode, String errorMessage) {
		this.errorCode=errorCode;
		this.errorMessage=errorMessage;
		
	}
	
}
