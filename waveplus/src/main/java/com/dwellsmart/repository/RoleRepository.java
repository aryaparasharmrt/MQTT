package com.dwellsmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dwellsmart.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>  {

}
