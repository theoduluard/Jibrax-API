package com.jibrax.service;

import com.jibrax.dao.ProjectDAO;
import com.jibrax.dao.TeamDAO;
import com.jibrax.dao.UserDAO;
import com.jibrax.domain.assignee.Assignee;
import com.jibrax.domain.project.Project;
import com.jibrax.domain.team.Team;
import com.jibrax.domain.user.User;
import com.jibrax.dto.project.CreateProjectDTO;
import com.jibrax.dto.project.ProjectResponseDTO;
import com.jibrax.dto.task.TaskResponseDTO;
import com.jibrax.exception.LeaderNotFoundException;
import com.jibrax.exception.ProjectNotFoundException;
import com.jibrax.mapper.ProjectMapper;
import com.jibrax.mapper.TaskMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;
    private final TeamDAO teamDAO;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;

    public ProjectService(ProjectDAO projectDAO, UserDAO userDAO, TeamDAO teamDAO, ProjectMapper projectMapper, TaskMapper taskMapper) {
        this.projectDAO = projectDAO;
        this.userDAO = userDAO;
        this.teamDAO = teamDAO;
        this.projectMapper = projectMapper;
        this.taskMapper = taskMapper;
    }

    public List<ProjectResponseDTO> getAllProjects() {
        return projectDAO.findAll()
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    public ProjectResponseDTO getProjectById(Long id) {
        return projectDAO.findById(id)
                .map(projectMapper::toResponseDTO)
                .orElse(null);
    }

    public List<ProjectResponseDTO> getProjectByLeaderId(Long leaderId) {
        Optional<User> userAssignee = userDAO.findById(leaderId);
        Optional<Team> teamAssignee = teamDAO.findById(leaderId);

        Assignee assignee;
        if(userAssignee.isPresent()) {
            assignee = userAssignee.get();
        }
        else if(teamAssignee.isPresent()) {
            assignee = teamAssignee.get();
        }
        else {
            throw new LeaderNotFoundException(leaderId);
        }

        return projectDAO.findByProjectLeader(assignee)
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    public List<TaskResponseDTO> getTasksByProjectId(Long projectId) {
        Project project = projectDAO.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return project.getTasks().stream()
                .map(taskMapper::toResponseDTO)
                .toList();
    }

    public ProjectResponseDTO createProject(CreateProjectDTO createDTO) {
        Project project = projectMapper.toEntity(createDTO);

        if (createDTO.getProjectLeaderId() != null) {
            Optional<User> userAssignee = userDAO.findById(createDTO.getProjectLeaderId());
            Optional<Team> teamAssignee = teamDAO.findById(createDTO.getProjectLeaderId());

            if (userAssignee.isPresent()) {
                project.setProjectLeader(userAssignee.get());
            }
            else if (teamAssignee.isPresent()) {
                project.setProjectLeader(teamAssignee.get());
            }
            else{
                throw new LeaderNotFoundException(createDTO.getProjectLeaderId());
            }
        }

        return projectMapper.toResponseDTO(projectDAO.save(project));
    }

    public ProjectResponseDTO updateProject(Long projectId, CreateProjectDTO updateDTO) {
        Project project = projectDAO.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        project.setProjectName(updateDTO.getProjectName());
        project.setProjectDescription(updateDTO.getProjectDescription());
        project.setProjectStartDate(updateDTO.getProjectStartDate());
        project.setProjectLeader(null);

        if (updateDTO.getProjectLeaderId() != null) {
            Optional<User> userAssignee = userDAO.findById(updateDTO.getProjectLeaderId());
            Optional<Team> teamAssignee = teamDAO.findById(updateDTO.getProjectLeaderId());

            if (userAssignee.isPresent()) {
                project.setProjectLeader(userAssignee.get());
            }
            else if (teamAssignee.isPresent()) {
                project.setProjectLeader(teamAssignee.get());
            }
            else{
                throw new LeaderNotFoundException(updateDTO.getProjectLeaderId());
            }
        }

        return projectMapper.toResponseDTO(projectDAO.save(project));
    }

    public void deleteProject(Long id) {
        if (!projectDAO.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        projectDAO.deleteById(id);
    }
}
