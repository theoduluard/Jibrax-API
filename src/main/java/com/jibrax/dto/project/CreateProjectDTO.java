package com.jibrax.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectDTO {
    @NotBlank(message = "The project's name is mandatory")
    private String projectName;

    private String projectDescription;

    @NotNull(message = "The start date of the project is mandatory")
    private Date projectStartDate;

    private Long projectLeaderId;
}
