package com.jibrax.controller;

import com.jibrax.dto.project.CreateProjectDTO;
import com.jibrax.dto.project.ProjectResponseDTO;
import com.jibrax.dto.task.TaskResponseDTO;
import com.jibrax.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projets", description = "Endpoints pour la gestion des projets")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Lister tous les projets", description = "Récupère la liste complète de tous les projets existants.")
    @ApiResponse(responseCode = "200", description = "Liste des projets récupérée avec succès")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un projet par son ID", description = "Récupère les détails d'un projet spécifique.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projet trouvé"),
            @ApiResponse(responseCode = "404", description = "Projet non trouvé")
    })
    public ResponseEntity<ProjectResponseDTO> getProjectById(
            @Parameter(description = "ID du projet") @PathVariable Long id) {
        ProjectResponseDTO projectResponseDTO = projectService.getProjectById(id);
        if(projectResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectResponseDTO);
    }

    @GetMapping("/leader/{id}")
    @Operation(summary = "Lister les projets par chef de projet", description = "Récupère tous les projets dirigés par un utilisateur spécifique.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des projets récupérée"),
            @ApiResponse(responseCode = "404", description = "Chef de projet ou projets non trouvés")
    })
    public ResponseEntity<List<ProjectResponseDTO>> getProjectLeaderById(
            @Parameter(description = "ID du chef de projet") @PathVariable Long id) {
        List<ProjectResponseDTO> projectResponseDTO = projectService.getProjectByLeaderId(id);
        if(projectResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectResponseDTO);
    }

    @GetMapping("/{id}/tasks")
    @Operation(summary = "Lister les tâches d'un projet", description = "Récupère toutes les tâches associées à un projet spécifique.")
    @ApiResponse(responseCode = "200", description = "Liste des tâches récupérée")
    public ResponseEntity<List<TaskResponseDTO>> getProjectTasks(
            @Parameter(description = "ID du projet") @PathVariable Long id) {
        List<TaskResponseDTO> projectTasks = projectService.getTasksByProjectId(id);
        return ResponseEntity.ok(projectTasks);
    }

    @PostMapping
    @Operation(summary = "Créer un projet", description = "Crée un nouveau projet à partir des données fournies.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projet créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody CreateProjectDTO dto) {
        return ResponseEntity.ok(projectService.createProject(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un projet", description = "Modifie les informations d'un projet existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projet mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Projet non trouvé")
    })
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @Parameter(description = "ID du projet à modifier") @PathVariable Long id,
            @Valid @RequestBody CreateProjectDTO dto) {
        return ResponseEntity.ok(projectService.updateProject(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un projet", description = "Supprime un projet de la base de données de manière permanente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projet supprimé"),
            @ApiResponse(responseCode = "404", description = "Projet non trouvé")
    })
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID du projet à supprimer") @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}