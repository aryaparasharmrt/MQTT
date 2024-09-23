package com.dwellsmart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.dto.request.CreateProjectRequest;
import com.dwellsmart.entity.Project;
import com.dwellsmart.service.IProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/project", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ProjectController {

	@Autowired
	private IProjectService projectService;

	
	@GetMapping("/{id}")
	public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
		Project project = projectService.getProjectById(id);
		return ResponseEntity.ok(project);
	}

	@PostMapping("/create")
	public ResponseEntity<String> createProject(@Valid @RequestBody CreateProjectRequest request) {
		
		projectService.createNewProject(request);
		return ResponseEntity.ok("Project created successfully");
	}

}
