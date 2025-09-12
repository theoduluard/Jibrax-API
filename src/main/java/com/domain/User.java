package com.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
public class User extends Assignee{

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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }



    @ManyToOne
    @JoinColumn(name = "team_id")
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @OneToOne(mappedBy = "teamLeader")
    public Team getLeadingTeam() {
        return leadingTeam;
    }

    public void setLeadingTeam(Team leadingTeam) {
        this.leadingTeam = leadingTeam;
    }
}
