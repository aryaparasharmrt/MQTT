package com.dwellsmart.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AccountTransactionResponse {

	private String transactionId;
	private Integer accountId;
	private BigDecimal amount;
	private String transactionType;
	private LocalDateTime transactionDate;
	private String status;

}
