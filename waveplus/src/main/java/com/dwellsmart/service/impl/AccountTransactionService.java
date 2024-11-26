package com.dwellsmart.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.ErrorCode;
import com.dwellsmart.dto.request.AccountTransactionRequest;
import com.dwellsmart.entity.Account;
import com.dwellsmart.entity.AccountTransaction;
import com.dwellsmart.entity.Project;
import com.dwellsmart.entity.Resident;
import com.dwellsmart.entity.User;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.repository.AccountTransactionRepository;
import com.dwellsmart.service.IAccountTransactionService;
import com.dwellsmart.util.TransactionModeMapper;

@Service
public class AccountTransactionService implements IAccountTransactionService {

	@Autowired
	private AccountTransactionRepository accountTransactionsRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@Override
	public List<AccountTransaction> getAllTransactions() {
		return accountTransactionsRepository.findAll();
	}

	@Override
	public Optional<AccountTransaction> getTransactionById(Long txnId) {
		return accountTransactionsRepository.findById(txnId);
	}

	@Override
	public AccountTransaction createTransaction(AccountTransaction transaction) {
		return accountTransactionsRepository.save(transaction);
	}

	@Override
	public AccountTransaction updateTransaction(Long txnId, AccountTransaction updatedTransaction) {
		return accountTransactionsRepository.findById(txnId).map(existingTransaction -> {
			existingTransaction.setTxnAmt(updatedTransaction.getTxnAmt());
			existingTransaction.setTxnDte(updatedTransaction.getTxnDte());
//            existingTransaction.setTxnType(updatedTransaction.getTxnType());
//            existingTransaction.setTxnMode(updatedTransaction.getTxnMode());
			// Update other fields as needed
			return accountTransactionsRepository.save(existingTransaction);
		}).orElseThrow(() -> new RuntimeException("Transaction not found with id " + txnId));
	}

	@Override
	public void deleteTransaction(Long txnId) {
		accountTransactionsRepository.deleteById(txnId);
	}

	public boolean isTxnUnique(AccountTransaction txn) {
		// TODO Auto-generated method stub
		return false;
	}

	public void processTransaction(AccountTransactionRequest transactionRequest) {
		if (!TransactionModeMapper.getAvailableModes(transactionRequest.getTransactionType())
				.contains(transactionRequest.getTransactionMode())) {
			throw new ApplicationException(ErrorCode.INVALID_TRANSACTION_MODE);
		}
		// yah pr resident ko nikalo
		Resident r = null;

		Optional<Account> accountById = accountService.getAccountById(transactionRequest.getAccountId());

		Account account = accountById.get();

		Optional<Project> projectById = projectService.getProjectById(transactionRequest.getProjectId());
		Project project = projectById.get();

		Optional<User> byId = userService.findById(transactionRequest.getUserId());
		User user = byId.get();

		AccountTransaction accountTransactions = AccountTransaction.builder().account(account).project(project)
				.transactionMode(transactionRequest.getTransactionMode())
				.transactionType(transactionRequest.getTransactionType()).txnAmt(transactionRequest.getAmount())
				.user(user).build();

		switch (transactionRequest.getTransactionType()) {
		case CREDIT:
			processCredit(account, accountTransactions);
			break;
		case DEBIT:
			processDebit(account, accountTransactions);
			break;
		case RECHARGE:
			processRecharge(account, accountTransactions);
			break;
//            default:
//                throw new IllegalArgumentException("Unknown transaction type");
		}
	}

	private void processCredit(Account account, AccountTransaction transaction) {
		System.out.println("Processing Credit Transaction: " + transaction);
		
		account.setAccountBalance(account.getAccountBalance()+transaction.getTxnAmt());
		
		accountTransactionsRepository.save(transaction);

		
		// Add logic for processing credit
	}

	private void processDebit(Account account, AccountTransaction transaction) {
		System.out.println("Processing Debit Transaction: " + transaction);

		account.setAccountBalance(account.getAccountBalance() - transaction.getTxnAmt());

		accountTransactionsRepository.save(transaction);
	}

	private void processRecharge(Account account, AccountTransaction transaction) {
		System.out.println("Processing Recharge Transaction: " + transaction);

		account.setAccountBalance(account.getAccountBalance() + transaction.getTxnAmt());

		accountTransactionsRepository.save(transaction);
	}
}
