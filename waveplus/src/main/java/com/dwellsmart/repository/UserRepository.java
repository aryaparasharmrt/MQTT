package com.dwellsmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dwellsmart.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

}
