package com.dwellsmart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dwellsmart.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

	// Method to find a single project by active status
	Optional<Project> findFirstByActiveStatus(Boolean activeStatus);

	// Method to find all projects by active status
	List<Project> findByActiveStatus(Boolean activeStatus);

}