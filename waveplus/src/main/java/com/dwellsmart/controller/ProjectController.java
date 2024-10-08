package com.dwellsmart.controller;

import static com.dwellsmart.constants.Endpoints.BASE;
import static com.dwellsmart.constants.Endpoints.PROJECTS;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dwellsmart.dto.request.CreateProjectRequest;
import com.dwellsmart.entity.Project;
import com.dwellsmart.service.IProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(BASE+ PROJECTS)
public class ProjectController {

	@Autowired
	private IProjectService projectService;

	

	@PostMapping
	@PreAuthorize("hasAuthority('MANAGER')")
	public ResponseEntity<String> createProject(@Valid @RequestBody CreateProjectRequest request) {
		
		projectService.createNewProject(request);
		return ResponseEntity.ok("Project created successfully");
	}
	
	 @GetMapping
	    public List<Project> getAllProjects() {
	        return projectService.getAllProjects();
	    }

	    @GetMapping("/{projectId}")
	    public ResponseEntity<Project> getProjectById(@PathVariable Integer projectId) {
	        Optional<Project> project = projectService.getProjectById(projectId);
	        return project.map(ResponseEntity::ok)
	                .orElse(ResponseEntity.notFound().build());
	    }

//	    @PostMapping
//	    public ResponseEntity<Project> createProject(@RequestBody Project project) {
//	        Project newProject = projectService.createProject(project);
//	        return ResponseEntity.ok(newProject);
//	    }

	    @PutMapping("/{projectId}")
	    public ResponseEntity<Project> updateProject(@PathVariable Integer projectId, @RequestBody Project updatedProject) {
	        try {
	            Project project = projectService.updateProject(projectId, updatedProject);
	            return ResponseEntity.ok(project);
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    @DeleteMapping("/{projectId}")
	    public ResponseEntity<Void> deleteProject(@PathVariable Integer projectId) {
	        projectService.deleteProject(projectId);
	        return ResponseEntity.noContent().build();
	    }
	
}
