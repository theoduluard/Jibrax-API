package com.jibrax.dto.task;

import com.jibrax.domain.task.TaskPriority;
import com.jibrax.domain.task.TaskStatus;
import com.jibrax.domain.task.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDTO {

    @NotBlank(message = "The name of the task is mandatory")
    private String taskName;

    private String description;

    @NotNull(message = "The task's priority is mandatory")
    private TaskPriority priority;

    @NotNull(message = "The task's status is mandatory")
    private TaskStatus status;

    @NotNull(message = "The task's type is mandatory")
    private TaskType type;

    private Long assigneeId;

    private Long projectId;
}