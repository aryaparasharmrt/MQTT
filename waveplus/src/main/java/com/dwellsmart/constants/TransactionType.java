package com.dwellsmart.constants;

public enum TransactionType {

	CREDIT, DEBIT, RECHARGE;

//	@JsonCreator
//	public static TransactionType fromValue(String value) {
//		try {
//			return TransactionType.valueOf(value.toUpperCase());
//		} catch (IllegalArgumentException e) {
//			throw new ApplicationException(HttpStatus.BAD_REQUEST,
//					ErrorCodeEnum.INVALID_TRANSACTION_TYPE.getErrorCode(),
//					ErrorCodeEnum.INVALID_TRANSACTION_TYPE.getErrorMessage());
//		}
//	}
}
