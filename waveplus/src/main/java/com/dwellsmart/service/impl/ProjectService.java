package com.dwellsmart.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.RoleType;
import com.dwellsmart.dto.request.CreateProjectRequest;
import com.dwellsmart.entity.Project;
import com.dwellsmart.entity.Role;
import com.dwellsmart.entity.Site;
import com.dwellsmart.entity.User;
import com.dwellsmart.repository.ProjectRepository;
import com.dwellsmart.service.IProjectService;
import com.dwellsmart.service.IUserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProjectService implements IProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private IUserService iUserService;

//	@Override
//	public Project getProjectById(Integer projectId) {
//		// findById returns an Optional<Project>
//		Optional<Project> projectOptional = projectRepository.findById(projectId);
//
//		// Handle case where project with the given ID does not exist
//		if (projectOptional.isPresent()) {
//			return projectOptional.get(); // If found, return the project
//		} else {
//			throw new EntityNotFoundException("Project with ID " + projectId + " not found.");
//		}
//	}

	@Override
	public boolean createNewProject(CreateProjectRequest createProject) {
		
		Project project = Project.builder().projectName(createProject.getProjectName()).emailId(createProject.getEmail()).
				phoneNumber(Long.parseLong(createProject.getPhoneNumber())).build();
//		projectRepository.save(project);
		
		Site site = Site.builder().siteName("Default").dgRate(0.0).gridRate(0.0).project(project).build();
		
		project.addSite(site);
		
		
		User user = User.builder().username(createProject.getUsername())
				.password(encoder.encode(createProject.getPassword())).email(createProject.getEmail()).phoneNumber(createProject.getPhoneNumber())
			.build();

		Role role = Role.builder().role(RoleType.ADMIN).assignedAt(LocalDateTime.now()).project(project).build();

		user.addRole(role); // Add role to the user
		
		iUserService.createNewUser(user); //with this project also created
		
		createProject(project);

		return true;
	}

	@Override
	public boolean isProjectActive(Integer projectId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	 @Override
	    public List<Project> getAllProjects() {
	        return projectRepository.findAll();
	    }

	    @Override
	    public Optional<Project> getProjectById(Integer projectId) {
	        return projectRepository.findById(projectId);
	    }

	    @Override
	    public Project createProject(Project project) {
	        return projectRepository.save(project);
	    }

	    @Override
	    public Project updateProject(Integer projectId, Project updatedProject) {
	        return projectRepository.findById(projectId).map(existingProject -> {
	            existingProject.setProjectName(updatedProject.getProjectName());
	            existingProject.setActiveStatus(updatedProject.getActiveStatus());
	            existingProject.setAutomaticBillingEnabled(updatedProject.getAutomaticBillingEnabled());
	            existingProject.setAutomaticReadingEnabled(updatedProject.getAutomaticReadingEnabled());
	            // Update other fields as needed
	            return projectRepository.save(existingProject);
	        }).orElseThrow(() -> new RuntimeException("Project not found with id " + projectId));
	    }

	    @Override
	    public void deleteProject(Integer projectId) {
	        projectRepository.deleteById(projectId);
	    }
}
