package com.jibrax.dto.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDTO {
    private Long id;
    private String teamname;
    private byte[] image;
    private List<TeamUserDTO> teamMembers = new ArrayList<>();
}
