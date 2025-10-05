package com.jibrax.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssigneeResponseDTO {
    private Long assigneeId;
    private String name;
    private byte[] image;
}
