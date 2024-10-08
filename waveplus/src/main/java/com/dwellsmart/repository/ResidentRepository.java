package com.dwellsmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dwellsmart.entity.Resident;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    // Additional query methods can be added here if needed
}