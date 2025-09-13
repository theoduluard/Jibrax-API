package com.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AssigneeTest {

    /**
     * Small concrete subclass used only for this junit test.
     */
    static class ConcreteAssignee extends Assignee {
        // no-op
    }

    @Test
    void assignee_gettersAndSetters_work() {
        ConcreteAssignee a = new ConcreteAssignee();

        Long id = 123L;
        String username = "test-assignee";
        byte[] image = new byte[]{1, 2, 3};
        boolean active = true;
        LocalDateTime created = LocalDateTime.of(2020, 1, 2, 3, 4, 5);
        LocalDateTime updated = LocalDateTime.of(2021, 6, 7, 8, 9, 10);

        a.setAssigneeId(id);
        a.setUsername(username);
        a.setImage(image);
        a.setActive(active);
        a.setCreatedAt(created);
        a.setUpdatedAt(updated);

        assertEquals(id, a.getAssigneeId());
        assertEquals(username, a.getUsername());
        assertArrayEquals(image, a.getImage());
        assertTrue(a.isActive());
        assertEquals(created, a.getCreatedAt());
        assertEquals(updated, a.getUpdatedAt());
    }
}
