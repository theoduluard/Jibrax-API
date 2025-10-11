package com.jibrax.controller;

import com.jibrax.dto.task.CreateTaskDTO;
import com.jibrax.dto.task.TaskResponseDTO;
import com.jibrax.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO taskResponseDTO = taskService.getTasksById(id);
        if(taskResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskResponseDTO);
    }

    @GetMapping("/project/{id}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByProjectId(@PathVariable Long id) {
        List<TaskResponseDTO> taskResponseDTOs = taskService.getTasksByProjectId(id);
        if(taskResponseDTOs == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskResponseDTOs);
    }

    @GetMapping("/assignee/{id}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByAssigneeId(@PathVariable Long id) {
        List<TaskResponseDTO> taskResponseDTOs = taskService.getTasksByAssigneeId(id);
        if(taskResponseDTOs == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskResponseDTOs);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskDTO dto) {
        return ResponseEntity.ok(taskService.createTask(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody CreateTaskDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}
