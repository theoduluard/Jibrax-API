package com.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("USER")
public class User extends Assignee implements Serializable {

    private String email;

    private String password;

    private String firstname;

    private String lastname;

    private Boolean isAdmin;

    private LocalDateTime lastLogin;

    private Team team;

    private Team leadingTeam;



    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    @ManyToOne
    @JoinColumn(name = "teamId")
    public Team getTeam() {
        return team;
    }

    @OneToOne(mappedBy = "teamLeader")
    public Team getLeadingTeam() {
        return leadingTeam;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setLeadingTeam(Team leadingTeam) {
        this.leadingTeam = leadingTeam;
    }
}
