package com.dwellsmart.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.entity.AccountTransactions;
import com.dwellsmart.service.impl.AccountTransactionsService;

@RestController
@RequestMapping("/api/transactions")
public class AccountTransactionsController {

    @Autowired
    private AccountTransactionsService accountTransactionsService;

    @GetMapping
    public List<AccountTransactions> getAllTransactions() {
        return accountTransactionsService.getAllTransactions();
    }

    @GetMapping("/{txnId}")
    public ResponseEntity<AccountTransactions> getTransactionById(@PathVariable Long txnId) {
        Optional<AccountTransactions> transaction = accountTransactionsService.getTransactionById(txnId);
        return transaction.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountTransactions> createTransaction(@RequestBody AccountTransactions transaction) {
        AccountTransactions newTransaction = accountTransactionsService.createTransaction(transaction);
        return ResponseEntity.ok(newTransaction);
    }

    @PutMapping("/{txnId}")
    public ResponseEntity<AccountTransactions> updateTransaction(@PathVariable Long txnId, @RequestBody AccountTransactions updatedTransaction) {
        try {
            AccountTransactions transaction = accountTransactionsService.updateTransaction(txnId, updatedTransaction);
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
}
