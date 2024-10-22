package com.dwellsmart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.constants.Endpoints;
import com.dwellsmart.dto.ResidentDTO;
import com.dwellsmart.entity.Resident;
import com.dwellsmart.service.impl.ProjectService;
import com.dwellsmart.service.impl.ResidentService;
import com.dwellsmart.service.impl.SiteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(Endpoints.BASE + Endpoints.RESIDENTS)
public class ResidentController {

	@Autowired
	private ResidentService residentService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private SiteService siteService;
	

	// Create a new Resident
	@PostMapping
	public ResponseEntity<String> addResident(@Valid @RequestBody ResidentDTO residentDTO) {
		boolean resident = residentService.addResident(residentDTO);
//		iResidentService.a
		return ResponseEntity.ok("successs");
	}

	

//	// Get all Residents (Optional)
//	@GetMapping
//	public ResponseEntity<List<Resident>> getAllResidents() {
//		List<Resident> residents = residentService.getAllResidents();
//		return new ResponseEntity<>(residents, HttpStatus.OK);
//	}
//
//	// Get Resident by ID
//	@GetMapping("/{id}")
//	public ResponseEntity<Resident> getResidentById(@PathVariable("id") Long residentId) {
//		Resident resident = residentService.getResidentById(residentId);
//		if (resident != null) {
//			return new ResponseEntity<>(resident, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//	// Update Resident details (Optional)
//	@PutMapping("/{id}")
//	public ResponseEntity<Resident> updateResident(@PathVariable("id") Long residentId,
//			@RequestBody Resident residentDetails) {
//		Resident updatedResident = residentService.updateResident(residentId, residentDetails);
//		return new ResponseEntity<>(updatedResident, HttpStatus.OK);
//	}

	// Delete Resident (Optional)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteResident(@PathVariable("id") Long residentId) {
		residentService.deleteResident(residentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
