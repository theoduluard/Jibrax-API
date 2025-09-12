package com.domain;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "TASKS")
public class Task implements Serializable {

    private Long taskId;

    private String taskName;

    private String description;



    private TaskPriority priority;

    private TaskStatus status;

    private TaskType type;



    private Assignee assignedUser;

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

    @Enumerated(EnumType.STRING)
    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }



    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne
    public Assignee getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(Assignee assignedUser) {
        this.assignedUser = assignedUser;
    }

}
