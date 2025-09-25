package com.dto;

import java.util.List;

public class TeamResponseDTO {
    private Long id;
    private String username;
    private byte[] image;
    private List<TeamMemberDTO> teamMembers;


    public Long getId() { return id; }

    public String getUsername() { return username; }

    public byte[] getImage() { return image; }

    public List<TeamMemberDTO> getTeamMembers() { return teamMembers; }


    public void setId(Long id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }

    public void setImage(byte[] image) { this.image = image; }

    public void setTeamMembers(List<TeamMemberDTO> teamMembers) { this.teamMembers = teamMembers; }
}
