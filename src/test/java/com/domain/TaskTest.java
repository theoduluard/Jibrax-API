package com.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testGettersAndSetters() {
        Task task = new Task();

        Long id = 10L;
        String name = "Implement Feature";
        String description = "Implement new functionality";
        TaskPriority priority = TaskPriority.HIGH;
        TaskStatus status = TaskStatus.IN_PROGRESS;
        TaskType type = TaskType.NEW_FEATURE;

        Assignee assignee = new User();
        Project project = new Project();

        task.setTaskId(id);
        task.setTaskName(name);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);
        task.setType(type);
        task.setAssignedUser(assignee);
        task.setProject(project);

        assertEquals(id, task.getTaskId());
        assertEquals(name, task.getTaskName());
        assertEquals(description, task.getDescription());
        assertEquals(priority, task.getPriority());
        assertEquals(status, task.getStatus());
        assertEquals(type, task.getType());
        assertEquals(assignee, task.getAssignedUser());
        assertEquals(project, task.getProject());
    }
}

