package com.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTeamDTO {
    @NotBlank(message = "Le nom d'utilisateur de l'équipe est obligatoire")
    private String username;

    private byte[] image;


    public String getUsername() { return username; }

    public byte[] getImage() { return image; }


    public void setUsername(String username) { this.username = username; }

    public void setImage(byte[] image) { this.image = image; }
}
