package com.jibrax.dto.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamUserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private byte[] image;
}
