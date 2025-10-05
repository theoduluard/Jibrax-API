package com.jibrax.dto.project;

import com.jibrax.domain.task.TaskPriority;
import com.jibrax.domain.task.TaskStatus;
import com.jibrax.domain.task.TaskType;
import com.jibrax.dto.task.AssigneeResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTaskDTO {
    private Long taskId;
    private String taskName;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private TaskType type;
    private AssigneeResponseDTO assigned;
}
