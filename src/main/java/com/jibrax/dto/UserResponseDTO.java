package com.jibrax.dto;

public class UserResponseDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String teamName;
    private byte[] image;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getImage() {
        return image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
