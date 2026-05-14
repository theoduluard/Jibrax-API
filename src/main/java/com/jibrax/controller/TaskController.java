package com.jibrax.controller;

import com.jibrax.dto.task.CreateTaskDTO;
import com.jibrax.dto.task.TaskResponseDTO;
import com.jibrax.service.TaskService;
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
@RequestMapping("/api/tasks")
@Tag(name = "Tâches", description = "Endpoints pour la gestion des tâches (tickets/issues)")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Lister toutes les tâches", description = "Récupère l'ensemble des tâches de l'application.")
    @ApiResponse(responseCode = "200", description = "Liste des tâches récupérée")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une tâche par son ID", description = "Récupère les détails d'une tâche spécifique.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tâche trouvée"),
            @ApiResponse(responseCode = "404", description = "Tâche non trouvée")
    })
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @Parameter(description = "ID de la tâche") @PathVariable Long id) {
        TaskResponseDTO taskResponseDTO = taskService.getTasksById(id);
        if(taskResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskResponseDTO);
    }

    @GetMapping("/project/{id}")
    @Operation(summary = "Lister les tâches d'un projet", description = "Récupère toutes les tâches appartenant à un projet spécifique.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des tâches récupérée"),
            @ApiResponse(responseCode = "404", description = "Projet ou tâches non trouvés")
    })
    public ResponseEntity<List<TaskResponseDTO>> getTasksByProjectId(
            @Parameter(description = "ID du projet") @PathVariable Long id) {
        List<TaskResponseDTO> taskResponseDTOs = taskService.getTasksByProjectId(id);
        if(taskResponseDTOs == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskResponseDTOs);
    }

    @GetMapping("/assignee/{id}")
    @Operation(summary = "Lister les tâches assignées à un utilisateur", description = "Récupère toutes les tâches dont un utilisateur spécifique est le responsable (assignee).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des tâches récupérée"),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou tâches non trouvés")
    })
    public ResponseEntity<List<TaskResponseDTO>> getTasksByAssigneeId(
            @Parameter(description = "ID de l'utilisateur assigné") @PathVariable Long id) {
        List<TaskResponseDTO> taskResponseDTOs = taskService.getTasksByAssigneeId(id);
        if(taskResponseDTOs == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskResponseDTOs);
    }

    @PostMapping
    @Operation(summary = "Créer une tâche", description = "Crée une nouvelle tâche et l'associe potentiellement à un projet et à un utilisateur.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tâche créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskDTO dto) {
        return ResponseEntity.ok(taskService.createTask(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une tâche", description = "Modifie le statut, la description ou l'assignation d'une tâche existante.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tâche mise à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Tâche non trouvée")
    })
    public ResponseEntity<TaskResponseDTO> updateTask(
            @Parameter(description = "ID de la tâche à modifier") @PathVariable Long id,
            @Valid @RequestBody CreateTaskDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une tâche", description = "Supprime définitivement une tâche du système.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tâche supprimée"),
            @ApiResponse(responseCode = "404", description = "Tâche non trouvée")
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID de la tâche à supprimer") @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}