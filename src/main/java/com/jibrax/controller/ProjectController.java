package com.jibrax.controller;

import com.jibrax.dto.project.CreateProjectDTO;
import com.jibrax.dto.project.ProjectResponseDTO;
import com.jibrax.dto.task.TaskResponseDTO;
import com.jibrax.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        ProjectResponseDTO projectResponseDTO = projectService.getProjectById(id);
        if(projectResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectResponseDTO);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getProjectTasks(@PathVariable Long id) {
        List<TaskResponseDTO> projectTasks = projectService.getTasksByProjectId(id);
        return ResponseEntity.ok(projectTasks);
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody CreateProjectDTO dto) {
        return ResponseEntity.ok(projectService.createProject(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody CreateProjectDTO dto) {
        return ResponseEntity.ok(projectService.updateProject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}
