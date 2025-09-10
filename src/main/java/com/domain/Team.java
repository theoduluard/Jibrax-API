package com.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity
public class Team extends Assignee {

    private User teamLeader;

    @OneToOne(fetch = FetchType.LAZY)
    public User getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(User teamLeader) {
        this.teamLeader = teamLeader;
    }


}
