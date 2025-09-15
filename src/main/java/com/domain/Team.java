package com.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@DiscriminatorValue("TEAM")
public class Team extends Assignee implements Serializable {

    private User teamLeader;

    private Collection<User> teamMembers;



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamLeaderId", unique = true)
    public User getTeamLeader() {
        return teamLeader;
    }

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Collection<User> getTeamMembers() {
        return teamMembers;
    }



    public void setTeamLeader(User leader) {
        this.teamLeader = leader;
    }

    public void setTeamMembers(Collection<User> teamMembers) {
        this.teamMembers = teamMembers;
    }
}
