package com.dwellsmart.service;

import java.util.List;
import java.util.Optional;

import com.dwellsmart.entity.AccountTransactions;

public interface IAccountTransactionsService {
    List<AccountTransactions> getAllTransactions();
    Optional<AccountTransactions> getTransactionById(Long txnId);
    AccountTransactions createTransaction(AccountTransactions transaction);
    AccountTransactions updateTransaction(Long txnId, AccountTransactions updatedTransaction);
    void deleteTransaction(Long txnId);
}
