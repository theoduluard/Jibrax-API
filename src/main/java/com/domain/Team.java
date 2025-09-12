package com.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "TEAMS")
public class Team extends Assignee {

    private User teamLeader;

    private Collection<User> teamMembers = new ArrayList<>();


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_leader_id", unique = true, nullable = false)
    public User getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(User leader) {
        this.teamLeader = leader;
        if (!teamMembers.contains(leader)) {
            teamMembers.add(leader);
            leader.setTeam(this);
        }
    }



    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Collection<User> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(Collection<User> teamMembers) {
        this.teamMembers = teamMembers;
    }
}
