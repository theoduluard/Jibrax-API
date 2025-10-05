package com.jibrax.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamDTO {
    private Long id;
    private String teamname;
    private byte[] image;
}
