package com.jibrax.mapper;

import com.jibrax.domain.assignee.Assignee;
import com.jibrax.domain.project.Project;
import com.jibrax.domain.task.Task;
import com.jibrax.domain.team.Team;
import com.jibrax.domain.user.User;
import com.jibrax.dto.project.CreateProjectDTO;
import com.jibrax.dto.project.ProjectResponseDTO;
import com.jibrax.dto.project.ProjectTaskDTO;
import com.jibrax.dto.task.AssigneeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "projectLeader", target = "projectLeader", qualifiedByName = "mapAssignee")
    @Mapping(source = "tasks", target = "tasks", qualifiedByName = "mapTasks")
    ProjectResponseDTO toResponseDTO(Project project);

    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "projectLeader", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project toEntity(CreateProjectDTO createProjectDTO);

    @Named("mapAssignee")
    default AssigneeResponseDTO mapAssignee(Assignee assignee) {
        if (assignee == null) {
            return null;
        }

        AssigneeResponseDTO dto = new AssigneeResponseDTO();
        dto.setAssigneeId(assignee.getAssigneeId());
        dto.setName(assignee.getUsername());
        dto.setImage(assignee.getImage());

        return dto;
    }

    @Named("mapTasks")
    default List<ProjectTaskDTO> mapTasks(Collection<Task> tasks) {
        if (tasks == null) {
            return Collections.emptyList();
        }

        return tasks.stream()
                .map(task -> {
                    ProjectTaskDTO dto = new ProjectTaskDTO();
                    dto.setTaskId(task.getTaskId());
                    dto.setTaskName(task.getTaskName());
                    dto.setDescription(task.getDescription());
                    dto.setPriority(task.getPriority());
                    dto.setStatus(task.getStatus());
                    dto.setType(task.getType());
                    dto.setAssigned(mapAssignee(task.getAssigned()));
                    return dto;
                })
                .toList();
    }
}