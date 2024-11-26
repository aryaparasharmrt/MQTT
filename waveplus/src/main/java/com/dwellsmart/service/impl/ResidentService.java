package com.dwellsmart.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.RoleType;
import com.dwellsmart.dto.ResidentDTO;
import com.dwellsmart.entity.Account;
import com.dwellsmart.entity.Project;
import com.dwellsmart.entity.Resident;
import com.dwellsmart.entity.Role;
import com.dwellsmart.entity.Site;
import com.dwellsmart.entity.User;
import com.dwellsmart.repository.ResidentRepository;
import com.dwellsmart.repository.RoleRepository;
import com.dwellsmart.service.IProjectService;
import com.dwellsmart.service.IResidentService;
import com.dwellsmart.service.ISiteService;
import com.dwellsmart.service.IUserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ResidentService implements  IResidentService{

	@Autowired
	private ResidentRepository residentRepository;
	
	@Autowired
	private IProjectService iProjectService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ISiteService iSiteService;
	
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired 
	private AccountService accountService;

	// Create or Save a Resident
//	@Transactional
//	public boolean addResident(ResidentDTO residentDTO) {
//		
//		Optional<Project> projectById = iProjectService.getProjectById(residentDTO.getProjectId());
//		Optional<Site> siteById = iSiteService.getSiteById(residentDTO.getSiteId());
//		
////		iUserService.createNewUser();
//
////		User user = User.builder().email(residentDTO.getEmail()).password(encoder.encode(residentDTO.getPassword()))
////				.username(residentDTO.getUsername()).build();
////		user.addRole(
////				Role.builder().role(RoleType.USER).assignedAt(LocalDateTime.now()).project(projectById.get()).build());
//
//		Resident resident = Resident.builder().project(projectById.get()).primaryOwnerName(residentDTO.getPrimaryOwnerName())
//    	.flatArea(residentDTO.getFlatArea())
//    	.emailId(residentDTO.getEmail())
//    	.flatNo(residentDTO.getFlatNo())
//				.meterId(residentDTO.getMeterId())
//    	.phoneNumber(residentDTO.getPhoneNumber())
////				.site(siteById.get()).user(user)
//				.build();
//
//		Account account = Account.builder().resident(resident).build();
////    	resident.addAccount(account);
//		resident.setAccount(account);
//
////		Resident newResident = residentService.addResident(resident);
////	}
//		
////		User user = User.builder().username("demo1").password("useme123").build();
////		
////		user.addRole(Role.builder().role(RoleType.USER).assignedAt(LocalDateTime.now()).project(resident.getProject()).build());
////		
//////		service.createNewUser(user);
////		
////		resident.setUser(user);
////		
////		// Create a new account with default values
////		Account account = new Account();
////		// Associate account with the resident
////		account.setResident(resident);
////		resident.setAccount(account);
////		
////		resident.setSite(resident.getProject().getDefaultSite());
//
////		return residentRepository.save(residentDTO)!=null;
//		return false;
//	}
	
	public String defaultUsername(Integer projectId, String flatNo) {
		return projectId + "_" + flatNo;
	}
	
	@Transactional
	public boolean addResident(ResidentDTO residentDTO) {
		

	    // Validate project existence
	    Project project = iProjectService.getProjectById(residentDTO.getProjectId())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));

//	     Validate site existence
	    Site site = iSiteService.getSiteById(residentDTO.getSiteId())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid site ID"));

	    List<Site> sites = project.getSites();
	    boolean contains = sites.contains(site);
	    if(!contains) {
	    	throw new RuntimeException("Invalid Site Id:");
	    }
	    
	    System.out.println("hii.....");
	    User user = null;
	    if (residentDTO.getUsername() != null && residentDTO.getPassword() != null) {
	        user = User.builder()
	                .email(residentDTO.getEmail())
	                .password(encoder.encode(residentDTO.getPassword()))
	                .username(residentDTO.getUsername()).phoneNumber(residentDTO.getPhoneNumber())
	                .build();
	    }else {
	    	user = User.builder().email(residentDTO.getEmail()).username(defaultUsername(residentDTO.getProjectId(),residentDTO.getFlatNo())).password(encoder.encode("useme123")).
	    			phoneNumber(residentDTO.getPhoneNumber()).build();
	    }
	        // Add role to the user
	   Role userRole =  Role.builder().role(RoleType.USER).assignedAt(LocalDateTime.now()).project(project).build();
	    
	        user.addRole(
	        		userRole
	        );

	        // Persist the user (if user service handles user creation)
	        iUserService.createNewUser(user);
	        roleRepository.save(userRole);
	        
	        
	        
	    

	    // Create Resident entity
	    Resident resident = Resident.builder()
	            .primaryOwnerName(residentDTO.getPrimaryOwnerName())
	            .secondaryOwnerName(residentDTO.getSecondaryOwnerName())
	            .flatNo(residentDTO.getFlatNo())
	            .flatArea(residentDTO.getFlatArea())
	            .phoneNumber(residentDTO.getPhoneNumber())
	            .emailId(residentDTO.getEmail())
//	            .meterRefId(residentDTO.getMeterId())
	            .project(project)
	            .site(site)
	            .user(user) // associate user if created
	            .build();

	    // Create Account entity and associate with Resident
	    Account account = Account.builder()
	            .resident(resident).accountBalance(residentDTO.getInitialAccountBalance()).lastReadingDate(LocalDateTime.now())
	            .build();

	    // Establish bidirectional relationship
	    resident.setAccount(account);
	    
	    

	    // Save the resident entity, which should cascade the save to the associated account
	    Resident savedResident = residentRepository.save(resident);
	    accountService.addNewAccount(account);

	    return savedResident != null;
	    
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

		resident.setPrimaryOwnerName(residentDetails.getPrimaryOwnerName());
//		resident.setEmailId(residentDetails.getEmailId());
//		resident.setPhoneNumber(residentDetails.getPhoneNumber());
//		resident.setFlatNo(residentDetails.getFlatNo());
//		resident.setFlatArea(residentDetails.getFlatArea());
//		resident.setMeterRefId(residentDetails.getMeterRefId());
		resident.setIsActive(residentDetails.getIsActive());

		return residentRepository.save(resident);
	}

	// Delete a Resident (Optional)
	@Transactional
	public void deleteResident(Long residentId) {
		Resident resident = residentRepository.findById(residentId)
				.orElseThrow(() -> new RuntimeException("Resident not found with id: " + residentId));
		residentRepository.delete(resident);
	}
	
	@Override
	public List<Resident> getActiveResidentsByProjectId(Integer projectId) {
		return residentRepository.findActiveResidentsByProjectId(projectId);
	}
	
	@Override
	public void saveResident(Resident resident) {
		residentRepository.save(resident);
	}
}