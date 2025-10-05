package com.jibrax.service;

import com.jibrax.dao.*;
import com.jibrax.domain.assignee.Assignee;
import com.jibrax.domain.project.Project;
import com.jibrax.domain.task.Task;
import com.jibrax.domain.team.Team;
import com.jibrax.domain.user.User;
import com.jibrax.dto.task.CreateTaskDTO;
import com.jibrax.dto.task.TaskResponseDTO;
import com.jibrax.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskDAO taskDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private TaskMapper taskMapper;

    public List<TaskResponseDTO> getAllTasks() {
        return taskDAO.findAll()
                .stream()
                .map(taskMapper::toResponseDTO)
                .toList();
    }

    public TaskResponseDTO getTasksById(long id) {
        return taskDAO.findById(id)
                .map(taskMapper::toResponseDTO)
                .orElse(null);
    }

    public List<TaskResponseDTO> getTasksByProject(Project project) {
        return taskDAO.findByProject(project)
                .stream()
                .map(taskMapper::toResponseDTO)
                .toList();
    }

    public List<TaskResponseDTO> getTasksByAssignee(Assignee assignee) {
        return taskDAO.findByAssigned(assignee)
                .stream()
                .map(taskMapper::toResponseDTO)
                .toList();
    }

    public TaskResponseDTO createTask(CreateTaskDTO createDTO) {
        Task task = taskMapper.toEntity(createDTO);

        if (createDTO.getProjectId() != null) {
            Project project = projectDAO.findById(createDTO.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found for ID: " + createDTO.getProjectId()));
            task.setProject(project);
        }

        if (createDTO.getAssigneeId() != null) {
            Optional<User> userAssignee = userDAO.findById(createDTO.getAssigneeId());
            Optional<Team> teamAssignee = teamDAO.findById(createDTO.getAssigneeId());

            if (userAssignee.isPresent()) {
                task.setAssigned(userAssignee.get());
            }
            else if (teamAssignee.isPresent()) {
                task.setAssigned(teamAssignee.get());
            }
            else{
                throw new IllegalArgumentException("Assignee not found with id " + createDTO.getAssigneeId());
            }
        }

        return taskMapper.toResponseDTO(taskDAO.save(task));
    }

    public TaskResponseDTO updateTask(Long taskId, CreateTaskDTO updateDTO) {
        Task task = taskDAO.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found for ID: " + taskId));

        task.setTaskName(updateDTO.getTaskName());
        task.setDescription(updateDTO.getDescription());
        task.setPriority(updateDTO.getPriority());
        task.setStatus(updateDTO.getStatus());
        task.setType(updateDTO.getType());
        task.setProject(null);
        task.setAssigned(null);

        if (updateDTO.getProjectId() != null) {
            Project project = projectDAO.findById(updateDTO.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found for ID: " + updateDTO.getProjectId()));
            task.setProject(project);
        }

        if (updateDTO.getAssigneeId() != null) {
            Optional<User> userAssignee = userDAO.findById(updateDTO.getAssigneeId());
            Optional<Team> teamAssignee = teamDAO.findById(updateDTO.getAssigneeId());

            if (userAssignee.isPresent()) {
                task.setAssigned(userAssignee.get());
            }
            else if (teamAssignee.isPresent()) {
                task.setAssigned(teamAssignee.get());
            }
            else{
                throw new IllegalArgumentException("Assignee not found with id " + updateDTO.getAssigneeId());
            }
        }

        return taskMapper.toResponseDTO(taskDAO.save(task));
    }

    public void deleteTask(Long id) {
        if (!taskDAO.existsById(id)) {
            throw new IllegalArgumentException("Task not found for ID: " + id);
        }
        taskDAO.deleteById(id);
    }
}
