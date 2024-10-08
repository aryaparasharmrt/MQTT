package com.dwellsmart.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.RoleType;
import com.dwellsmart.entity.Account;
import com.dwellsmart.entity.Resident;
import com.dwellsmart.entity.Role;
import com.dwellsmart.entity.User;
import com.dwellsmart.repository.AccountRepository;
import com.dwellsmart.repository.ResidentRepository;

import jakarta.transaction.Transactional;

@Service
public class ResidentService {

	@Autowired
	private ResidentRepository residentRepository;

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserService service;

	// Create or Save a Resident
	@Transactional
	public Resident createResident(Resident resident) {
		
		User user = User.builder().username("demo1").password("useme123").build();
		
		user.addRole(Role.builder().role(RoleType.USER).assignedAt(LocalDateTime.now()).project(resident.getProject()).build());
		
//		service.createNewUser(user);
		
		resident.setUser(user);
		
		// Create a new account with default values
		Account account = new Account();
		// Associate account with the resident
		account.setResident(resident);
		resident.setAccount(account);
		
		resident.setSite(resident.getProject().getDefaultSite());

		return residentRepository.save(resident);
	}

	// Get all residents (Optional)
	public List<Resident> getAllResidents() {
		return residentRepository.findAll();
	}

	// Get a resident by ID
	public Resident getResidentById(Long residentId) {
		return residentRepository.findById(residentId).orElse(null);
	}

	// Update Resident details (Optional)
	@Transactional
	public Resident updateResident(Long residentId, Resident residentDetails) {
		Resident resident = residentRepository.findById(residentId)
				.orElseThrow(() -> new RuntimeException("Resident not found with id: " + residentId));

		resident.setCustomerName(residentDetails.getCustomerName());
		resident.setEmailId(residentDetails.getEmailId());
		resident.setPhoneNumber(residentDetails.getPhoneNumber());
		resident.setFlatNo(residentDetails.getFlatNo());
		resident.setFlatArea(residentDetails.getFlatArea());
		resident.setMeterId(residentDetails.getMeterId());
		resident.setStatus(residentDetails.getStatus());

		return residentRepository.save(resident);
	}

	// Delete a Resident (Optional)
	@Transactional
	public void deleteResident(Long residentId) {
		Resident resident = residentRepository.findById(residentId)
				.orElseThrow(() -> new RuntimeException("Resident not found with id: " + residentId));
		residentRepository.delete(resident);
	}
}