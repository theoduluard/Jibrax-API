package com.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @Test
    void testGettersAndSetters() {
        Team team = new Team();

        User leader = new User();
        leader.setUsername("LeaderUser");
        User member = new User();
        member.setUsername("MemberUser");
        team.setTeamLeader(leader);

        assertEquals(leader, team.getTeamLeader());

        team.setTeamMembers(List.of(leader, member));

        assertEquals(2, team.getTeamMembers().size());
        assertTrue(team.getTeamMembers().contains(member));
    }
}
