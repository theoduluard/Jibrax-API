package com.domain;

import jakarta.persistence.*;

public class Task {

    private Long taskId;

    private String taskName;

    private String description;

    private Assignee assignedUser;

    private TaskPriority priority;

    private TaskStatus status;

    private TaskType type;

    private Project project;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long id) {
        this.taskId = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    public Assignee getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(Assignee assignedUser) {
        this.assignedUser = assignedUser;
    }

    @Enumerated(EnumType.STRING)
    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    @Enumerated(EnumType.STRING)
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @ManyToOne
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Enumerated(EnumType.STRING)
    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }
}
