package com.dwellsmart.service;

import java.util.List;
import java.util.Optional;

import com.dwellsmart.dto.request.CreateProjectRequest;
import com.dwellsmart.entity.Project;

public interface IProjectService {

	boolean createNewProject(CreateProjectRequest createProject);

	boolean isProjectActive(Integer projectId);
	
	
	List<Project> getAllProjects();
    Optional<Project> getProjectById(Integer projectId);
    Project createProject(Project project);
    Project updateProject(Integer projectId, Project updatedProject);
    void deleteProject(Integer projectId);

}
