package com.dwellsmart.constants;


import lombok.Getter;

public enum ErrorCode {
	
	INVALID_JSON("ERR001", "Invalid JSON "),
    MISSING_FIELD("ERR002", "Validation failed: Missing required field"),
    INVALID_IP_FORMAT("ERR003", "Invalid IP address format"),
    EMPTY_METERS("ERR004", "Meters list cannot be empty"),
    INVALID_METER_ID("ERR005", "Meter ID must be between 0 and 255"),
    
    
	GENERIC_EXCEPTION("10000", "Something went wrong, please try later"),

    TOKEN_EXPIRED("10001", "Authentication failed: Token has expired."),
    TOKEN_NOT_YET_VALID("10002", "Authentication failed: Token is not yet valid."),
    UNSUPPORTED_TOKEN("10003", "Bad request: Unsupported token format."),
    MALFORMED_TOKEN("10004", "Bad request: Malformed token."),
    INVALID_SIGNATURE("10005", "Authentication failed: Invalid token signature."),
    INVALID_ISSUER("10006", "Authentication failed: Token issuer is invalid."),
    INVALID_TOKEN("10007", "Authentication failed: Token is invalid."),
    AUTHENTICATION_FAILED("10008", "Authentication failed."),
    
    USER_NOT_FOUND("10009", "User not found"),
	
//	SIGNATURE_NOT_FOUND("10002","Bad request, signature not found"),
//	SIGNATURE_ALTERED("10003","Unauthorized request, data altered"),
//	
//	EXCEPTION_IN_SIGNATURE_CALCULATION("10004","Something went wrong, Signature calculation failed"),
//	AMOUNT_VALIDATION_FAILED("10005","Bad request, given amount parameter is not valid or empty"),
//	CURRENCY_VALIDATION_FAILED("10006","Bad request, given currency parameter is not valid or empty"),
//	EMAIL_VALIDATION_FAILED("10007","Bad request, given email parameter is not valid or empty"),
//	FIRSTNAME_VALIDATION_FAILED("10008","Bad request, given firstName parameter is not valid or empty"),
//	LASTNAME_VALIDATION_FAILED("10009","Bad request, given lastName parameter is not valid or empty"),
//	MERCHANT_TRANSACTION_REFERENCE_VALIDATION_FAILED("10010","Bad request, given merchantTransactionReference parameter is not valid or empty"),
//	PAYMENT_METHOD_VALIDATION_FAILED("10011","Bad request, given paymentMethod parameter is not valid or empty"),
//	PAYMENT_TYPE_VALIDATION_FAILED("10012","Bad request, given paymentType parameter is not valid or empty"),
//	PAYMENT_REQUEST_VALIDATION_FAILED("10013","Bad request, given request is empty"),
//	PAYMENT_VALIDATION_FAILED("10014","Bad request, given payment object is empty"),
//	USER_VALIDATION_FAILED("10015","Bad request, given user object is empty"),
//	PHONE_NUMBER_VALIDATION_FAILED("10016","Bad request, given phoneNumber parameter is not valid or empty"),
//	PROVIDER_ID_VALIDATION_FAILED("10017","Bad request, given proiderId parameter is not valid or empty"),
//	DUPLICATE_TRANSACTION("10018","Bad request, duplicate merchantTransactionReference"),
//	FAILED_TO_CREATE_USER("10019", "Something went wrong, User creation failed"),
//	FAILED_TO_CREATE_TRANSACTION("10020", "Something went wrong, Payment creation failed"),
//
//	CREDITOR_ACCOUNT_NUMBER_VALIDATION_FAILED("10022", "Bad request, given creditorAccountNumber parameter is not valid or empty"),
//
	INVALID_TRANSACTION_TYPE("10021", "Bad request: The transaction type is not valid or missing."),
	INVALID_TRANSACTION_MODE("10022", "Bad request: The transaction mode is not valid or missing."),
	INVALID_TRANSACTION_MODE_FOR_CREDIT_OR_DEBIT("10023", "Bad request: For CREDIT or DEBIT transactions, only MANUAL mode is allowed."),
//	INVALID_TRANSACTION_MODE_FOR_RECHARGE("10024", "Bad request: For RECHARGE transactions, only ONLINE, CASH, CHEQUE, or NEFT modes are allowed."),
	INVALID_REFRESH_TOKEN_OR_DEVICE_ID("10025","Invalid Refresh Token or Device Id.");
	
	
	



//	HTTP_METHOD_NOT_FOUND("10030", "Http method not found"),
//
//	HTTP_METHOD_NOT_SUPPORTED("10031","Http method not supported");
	
	@Getter
	private final String errorCode;
	@Getter
	private final String errorMessage;
	
	private ErrorCode(String errorCode, String errorMessage) {
		this.errorCode=errorCode;
		this.errorMessage=errorMessage;
		
	}
	
}
