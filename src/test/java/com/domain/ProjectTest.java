package com.domain;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectTest {

    @Test
    void testGettersAndSetters() {
        Project project = new Project();

        Long id = 1L;
        String name = "Test Project";
        String description = "Description";
        Date startDate = new Date();
        Assignee leader = new User();
        Task task = new Task();

        project.setProjectId(id);
        project.setProjectName(name);
        project.setProjectDescription(description);
        project.setProjectStartDate(startDate);
        project.setProjectLeader(leader);
        project.setTasks(List.of(task));

        assertEquals(id, project.getProjectId());
        assertEquals(name, project.getProjectName());
        assertEquals(description, project.getProjectDescription());
        assertEquals(startDate, project.getProjectStartDate());
        assertEquals(leader, project.getProjectLeader());
        assertTrue(project.getTasks().contains(task));
    }
}
