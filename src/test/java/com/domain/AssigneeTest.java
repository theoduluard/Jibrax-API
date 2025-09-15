package com.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

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
        Project project = new Project();
        Task task = new Task();

        a.setAssigneeId(id);
        a.setUsername(username);
        a.setImage(image);
        a.setActive(active);
        a.setCreatedAt(created);
        a.setUpdatedAt(updated);
        a.setTaskAssigned(List.of(task));
        a.setProjectAssigned(List.of(project));

        assertEquals(id, a.getAssigneeId());
        assertEquals(username, a.getUsername());
        assertArrayEquals(image, a.getImage());
        assertTrue(a.isActive());
        assertEquals(created, a.getCreatedAt());
        assertEquals(updated, a.getUpdatedAt());
        assertEquals(List.of(task), a.getTaskAssigned());
        assertEquals(List.of(project), a.getProjectAssigned());
    }
}
