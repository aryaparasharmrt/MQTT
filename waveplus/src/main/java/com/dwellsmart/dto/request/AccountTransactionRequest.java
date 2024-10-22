package com.dwellsmart.dto.request;

import com.dwellsmart.constants.TransactionMode;
import com.dwellsmart.constants.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountTransactionRequest {

	@NotNull
	private Double amount;

	@NotNull
	@JsonProperty("transaction_type")
	private TransactionType transactionType;

	@NotNull
	@JsonProperty("transaction_mode")
	private TransactionMode transactionMode;

	private String description;

	@NotNull
	@JsonProperty("account_id")
	private Long accountId;

	@NotNull
	@JsonProperty("user_id")
	private Long userId;  //For Admin

	@NotNull
	@JsonProperty("project_id")
	private Integer projectId;

}
