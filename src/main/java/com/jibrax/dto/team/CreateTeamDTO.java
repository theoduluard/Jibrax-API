package com.jibrax.dto.team;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamDTO {
    @NotBlank(message = "The team name is mandatory")
    private String teamname;

    private byte[] image;
}
