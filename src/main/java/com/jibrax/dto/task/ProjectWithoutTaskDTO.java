package com.jibrax.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWithoutTaskDTO {
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private Date projectStartDate;
    private AssigneeResponseDTO projectLeader;
}
