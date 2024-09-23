package com.dwellsmart.service;

import com.dwellsmart.dto.request.CreateProjectRequest;
import com.dwellsmart.entity.Project;

public interface IProjectService {

	Project getProjectById(Integer projectId);
	
	boolean createNewProject(CreateProjectRequest createProject);

}
