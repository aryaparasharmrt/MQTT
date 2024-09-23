package com.dwellsmart.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwellsmart.dto.request.CreateProjectRequest;
import com.dwellsmart.entity.Project;
import com.dwellsmart.repository.ProjectRepository;
import com.dwellsmart.service.IProjectService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProjectService implements IProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public Project getProjectById(Integer projectId) {
		// findById returns an Optional<Project>
		Optional<Project> projectOptional = projectRepository.findById(projectId);

		// Handle case where project with the given ID does not exist
		if (projectOptional.isPresent()) {
			return projectOptional.get(); // If found, return the project
		} else {
			throw new EntityNotFoundException("Project with ID " + projectId + " not found.");
		}
	}

	@Override
	public boolean createNewProject(CreateProjectRequest createProject) {
		
//		Project project = Project.builder().projectName("first").emailId("anshul.goyal@gmail.com").build();
//		projectRepository.saveAndFlush(project);
		return false;
	}
}
