package com.dwellsmart.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwellsmart.entity.AccountTransactions;
import com.dwellsmart.repository.AccountTransactionsRepository;
import com.dwellsmart.service.IAccountTransactionsService;

@Service
public class AccountTransactionsService implements IAccountTransactionsService {

    @Autowired
    private AccountTransactionsRepository accountTransactionsRepository;

    @Override
    public List<AccountTransactions> getAllTransactions() {
        return accountTransactionsRepository.findAll();
    }

    @Override
    public Optional<AccountTransactions> getTransactionById(Long txnId) {
        return accountTransactionsRepository.findById(txnId);
    }

    @Override
    public AccountTransactions createTransaction(AccountTransactions transaction) {
        return accountTransactionsRepository.save(transaction);
    }

    @Override
    public AccountTransactions updateTransaction(Long txnId, AccountTransactions updatedTransaction) {
        return accountTransactionsRepository.findById(txnId).map(existingTransaction -> {
            existingTransaction.setTxnAmt(updatedTransaction.getTxnAmt());
            existingTransaction.setTxnDte(updatedTransaction.getTxnDte());
            existingTransaction.setTxnType(updatedTransaction.getTxnType());
            existingTransaction.setTxnMode(updatedTransaction.getTxnMode());
            // Update other fields as needed
            return accountTransactionsRepository.save(existingTransaction);
        }).orElseThrow(() -> new RuntimeException("Transaction not found with id " + txnId));
    }

    @Override
    public void deleteTransaction(Long txnId) {
        accountTransactionsRepository.deleteById(txnId);
    }
}
