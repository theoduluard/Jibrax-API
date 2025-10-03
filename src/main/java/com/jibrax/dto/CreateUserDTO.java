package com.jibrax.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public class CreateUserDTO {
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstname;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastname;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @JsonProperty("team")
    private Long teamId;

    private byte[] image;

    public String getFirstname() { return firstname; }

    public String getLastname() { return lastname; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public Long getTeamId() { return teamId; }

    public byte[] getImage() { return image; }



    public void setFirstname(String firstname) { this.firstname = firstname; }

    public void setLastname(String lastname) { this.lastname = lastname; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public void setImage(byte[] image) { this.image = image; }
}