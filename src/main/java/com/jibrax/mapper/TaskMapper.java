package com.jibrax.mapper;

import com.jibrax.domain.task.Task;
import com.jibrax.dto.task.CreateTaskDTO;
import com.jibrax.dto.task.TaskResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponseDTO toResponseDTO(Task task);

    Task toEntity(CreateTaskDTO taskDTO);
}
