package com.dwellsmart.util;

import java.util.EnumSet;
import java.util.Set;

import org.springframework.http.HttpStatus;

import com.dwellsmart.constants.ErrorCodeEnum;
import com.dwellsmart.constants.TransactionMode;
import com.dwellsmart.constants.TransactionType;
import com.dwellsmart.exception.ApplicationException;

public class TransactionModeMapper {
	
	public static Set<TransactionMode> getAvailableModes(TransactionType type) {
        switch (type) {
            case CREDIT:
                return EnumSet.of(TransactionMode.MANUAL, TransactionMode.AUTO);
            case DEBIT:
                return EnumSet.of(TransactionMode.MANUAL, TransactionMode.AUTO);
            case RECHARGE:
                return EnumSet.of(TransactionMode.ONLINE, TransactionMode.CASH, TransactionMode.CHEQUE, TransactionMode.NEFT);
            default:
                throw new ApplicationException(HttpStatus.BAD_REQUEST,ErrorCodeEnum.INVALID_TRANSACTION_TYPE.getErrorCode(),ErrorCodeEnum.INVALID_TRANSACTION_TYPE.getErrorMessage());
        }
    }

}
