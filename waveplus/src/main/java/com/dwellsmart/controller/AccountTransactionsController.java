package com.dwellsmart.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.constants.Endpoints;
import com.dwellsmart.constants.ErrorCodeEnum;
import com.dwellsmart.constants.TransactionMode;
import com.dwellsmart.constants.TransactionType;
import com.dwellsmart.dto.request.AccountTransactionRequest;
import com.dwellsmart.entity.AccountTransaction;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.service.impl.AccountTransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(Endpoints.BASE+Endpoints.ACCOUNT_TRANSACTIONS)
public class AccountTransactionsController {

    @Autowired
    private AccountTransactionService accountTransactionsService;

    @GetMapping
    public List<AccountTransaction> getAllTransactions() {
        return accountTransactionsService.getAllTransactions();
    }

    @GetMapping("/{txnId}")
    public ResponseEntity<AccountTransaction> getTransactionById(@PathVariable Long txnId) {
    	
    	System.out.println(txnId);
//        Optional<AccountTransactions> transaction = accountTransactionsService.getTransactionById(txnId);
//        return transaction.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
    	return null;
    }

//    @PostMapping
//    public ResponseEntity<AccountTransactions> createTransaction(@RequestBody AccountTransactions transaction) {
//        AccountTransactions newTransaction = accountTransactionsService.createTransaction(transaction);
//        return ResponseEntity.ok(newTransaction);
//    }
    
	@PostMapping
	public ResponseEntity<?> createTransaction(@Valid @RequestBody AccountTransactionRequest transactionRequest) {

		// For CREDIT or DEBIT, only MANUAL mode is allowed
		if ((transactionRequest.getTransactionType() == TransactionType.CREDIT
				|| transactionRequest.getTransactionType() == TransactionType.DEBIT)
				&& transactionRequest.getTransactionMode() != TransactionMode.MANUAL) {
			throw new ApplicationException(HttpStatus.BAD_REQUEST,
					ErrorCodeEnum.INVALID_TRANSACTION_MODE_FOR_CREDIT_OR_DEBIT.getErrorCode(),
					ErrorCodeEnum.INVALID_TRANSACTION_MODE_FOR_CREDIT_OR_DEBIT.getErrorMessage());
		}

//		Already logic implement in trasaction mode mapper class check only for auto mode 
//		if (transactionRequest.getTransactionType() == TransactionType.RECHARGE) {
//			if (transactionRequest.getTransactionMode() != TransactionMode.ONLINE
//					&& transactionRequest.getTransactionMode() != TransactionMode.CASH
//					&& transactionRequest.getTransactionMode() != TransactionMode.CHEQUE
//					&& transactionRequest.getTransactionMode() != TransactionMode.NEFT) {
//
//				throw new ApplicationException(HttpStatus.BAD_REQUEST,
//						ErrorCodeEnum.INVALID_TRANSACTION_MODE_FOR_RECHARGE.getErrorCode(),
//						ErrorCodeEnum.INVALID_TRANSACTION_MODE_FOR_RECHARGE.getErrorMessage());
//			}
//			//For recharges related logic
//			
//		}


    	accountTransactionsService.processTransaction(transactionRequest);
        return ResponseEntity.ok("success");
	}

    @PutMapping("/{txnId}")
    public ResponseEntity<AccountTransaction> updateTransaction(@PathVariable Long txnId, @RequestBody AccountTransaction updatedTransaction) {
        try {
            AccountTransaction transaction = accountTransactionsService.updateTransaction(txnId, updatedTransaction);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{txnId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long txnId) {
        accountTransactionsService.deleteTransaction(txnId);
        return ResponseEntity.noContent().build();
    }
    
    
//    public ResponseEntity<AccountTransactionResponse> createTransaction(
//            @RequestBody AccountTransactionRequest request) {
//    	
//    	
//
//        // Dummy logic for creating a transaction
//        AccountTransactionResponse response = new AccountTransactionResponse();
//        response.setTransactionId("txn123");
//        response.setAccountId(request.getAccountId());
////        response.setAmount(request.getAmount());
////        response.setTransactionType(request.getTransactionType());
//        response.setTransactionDate(LocalDateTime.now());
//        response.setStatus("SUCCESS");
//
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
}
