package com.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGettersAndSetters() {
        User user = new User();
        Team team = new Team();
        Team leadingTeam = new Team();

        user.setEmail("test@example.com");
        user.setPassword("secret");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setAdmin(true);
        user.setLastLogin(LocalDateTime.now());
        user.setTeam(team);
        user.setLeadingTeam(leadingTeam);

        assertEquals("test@example.com", user.getEmail());
        assertEquals("secret", user.getPassword());
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        assertTrue(user.getAdmin());
        assertNotNull(user.getLastLogin());
        assertEquals(team, user.getTeam());
        assertEquals(leadingTeam, user.getLeadingTeam());
    }
}
