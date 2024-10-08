package com.dwellsmart.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.constants.RoleType;
import com.dwellsmart.dto.ResidentDTO;
import com.dwellsmart.entity.Account;
import com.dwellsmart.entity.Project;
import com.dwellsmart.entity.Resident;
import com.dwellsmart.entity.Role;
import com.dwellsmart.entity.Site;
import com.dwellsmart.entity.User;
import com.dwellsmart.service.impl.ProjectService;
import com.dwellsmart.service.impl.ResidentService;
import com.dwellsmart.service.impl.SiteService;

@RestController
@RequestMapping("/api/v1/residents")
public class ResidentController {

    @Autowired
    private ResidentService residentService;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private SiteService siteService;

    // Create a new Resident
    @PostMapping
    public ResponseEntity<String> createResident(@RequestBody ResidentDTO residentDTO) {
    	Optional<Project> projectById = projectService.getProjectById(residentDTO.getProjectId());
    	Optional<Site> siteById = siteService.getSiteById(residentDTO.getSiteId());
    	
    	User user = User.builder().email(residentDTO.getEmailId()).password(residentDTO.getPassword()).username(residentDTO.getUsername()).build();
    	user.addRole(Role.builder().role(RoleType.USER).assignedAt(LocalDateTime.now()).project(projectById.get()).build());
    	
    	
    	Resident resident = Resident.builder().project(projectById.get()).customerName(residentDTO.getCustomerName())
    	.flatArea(residentDTO.getFlatArea()).emailId(residentDTO.getEmailId()).
    	flatNo(residentDTO.getFlatNo()).meterId(residentDTO.getMeterId()).phoneNumber(residentDTO.getPhoneNumber()).site(siteById.get()).user(user).build();
    	
    	Account account = Account.builder().build();
    	resident.addAccount(account);
    	
    	
    	
    	
        Resident newResident = residentService.createResident(resident);
//        User.builder().
        return ResponseEntity.ok("created...");
//        return new ResponseEntity<>(newResident, HttpStatus.CREATED);
    }

    // Get all Residents (Optional)
    @GetMapping
    public ResponseEntity<List<Resident>> getAllResidents() {
        List<Resident> residents = residentService.getAllResidents();
        return new ResponseEntity<>(residents, HttpStatus.OK);
    }

    // Get Resident by ID
    @GetMapping("/{id}")
    public ResponseEntity<Resident> getResidentById(@PathVariable("id") Long residentId) {
        Resident resident = residentService.getResidentById(residentId);
        if (resident != null) {
            return new ResponseEntity<>(resident, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update Resident details (Optional)
    @PutMapping("/{id}")
    public ResponseEntity<Resident> updateResident(@PathVariable("id") Long residentId, @RequestBody Resident residentDetails) {
        Resident updatedResident = residentService.updateResident(residentId, residentDetails);
        return new ResponseEntity<>(updatedResident, HttpStatus.OK);
    }

    // Delete Resident (Optional)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResident(@PathVariable("id") Long residentId) {
        residentService.deleteResident(residentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
