package com.jibrax.dto.project;

import com.jibrax.dto.task.AssigneeResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private Date projectStartDate;
    private AssigneeResponseDTO projectLeader;
    private List<ProjectTaskDTO> tasks = new ArrayList<>();
}
