package com.dwellsmart.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dwellsmart.entity.Account;
import com.dwellsmart.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Method to retrieve account details by accountId
    public Optional<Account> getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    // Method to update the account balance
    @Transactional
    public Account updateAccountBalance(Long accountId, BigDecimal newBalance) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setAccountBalance(newBalance);
            account.setLastUpdatedDate(new java.util.Date());
            return accountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + accountId);
        }
    }

    // Additional methods for account management, such as credit/debit, can be added here
}
