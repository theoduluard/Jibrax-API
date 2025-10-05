package com.jibrax.dto.task;

import com.jibrax.domain.task.TaskPriority;
import com.jibrax.domain.task.TaskStatus;
import com.jibrax.domain.task.TaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private Long taskId;
    private String taskName;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private TaskType type;
    private AssigneeResponseDTO assigned;
    private ProjectWithoutTaskDTO project;
}