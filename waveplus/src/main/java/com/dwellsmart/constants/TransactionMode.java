package com.dwellsmart.constants;

public enum TransactionMode {

	MANUAL, AUTO, ONLINE, CASH, CHEQUE, NEFT;

//	@JsonCreator(mode = Mode.PROPERTIES)
//	public static TransactionType fromValue(String value) {
//		try {
//			return TransactionType.valueOf(value.toUpperCase());
//		} catch (IllegalArgumentException e) {
//			throw new ApplicationException(HttpStatus.BAD_REQUEST,
//					ErrorCodeEnum.INVALID_TRANSACTION_MODE.getErrorCode(),
//					ErrorCodeEnum.INVALID_TRANSACTION_MODE.getErrorMessage());
//		}
//	}


}
