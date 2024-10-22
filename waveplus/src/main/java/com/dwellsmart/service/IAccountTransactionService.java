package com.dwellsmart.service;

import java.util.List;
import java.util.Optional;

import com.dwellsmart.entity.AccountTransaction;

public interface IAccountTransactionService {
    List<AccountTransaction> getAllTransactions();
    Optional<AccountTransaction> getTransactionById(Long txnId);
    AccountTransaction createTransaction(AccountTransaction transaction);
    AccountTransaction updateTransaction(Long txnId, AccountTransaction updatedTransaction);
    void deleteTransaction(Long txnId);
}
