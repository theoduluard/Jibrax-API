package com.dto;

public class TeamMemberDTO {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private byte[] image;


    public Long getId() { return id; }

    public String getUsername() { return username; }

    public String getFirstname() { return firstname; }

    public String getLastname() { return lastname; }

    public String getEmail() { return email; }

    public byte[] getImage() { return image; }


    public void setId(Long id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }

    public void setFirstname(String firstname) { this.firstname = firstname; }

    public void setLastname(String lastname) { this.lastname = lastname; }

    public void setEmail(String email) { this.email = email; }

    public void setImage(byte[] image) { this.image = image; }
}
