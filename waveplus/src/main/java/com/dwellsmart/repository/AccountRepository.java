package com.dwellsmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dwellsmart.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}