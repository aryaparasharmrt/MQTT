package com.dwellsmart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dwellsmart.entity.Resident;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    // Additional query methods can be added here if needed
	
	 // Custom query to fetch only active residents for a given project
    @Query("SELECT r FROM Resident r WHERE r.project.id = :projectId AND r.isActive = true")
    List<Resident> findActiveResidentsByProjectId(@Param("projectId") Integer projectId);
}