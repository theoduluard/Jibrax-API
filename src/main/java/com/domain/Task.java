package com.domain;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Tasks")
public class Task {

    private Long taskId;

    private String taskName;

    private String description;

    private TaskPriority priority;

    private TaskStatus status;

    private TaskType type;

    private Assignee assigned;

    private Project project;



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    @Enumerated(EnumType.STRING)
    public TaskPriority getPriority() {
        return priority;
    }

    @Enumerated(EnumType.STRING)
    public TaskStatus getStatus() {
        return status;
    }

    @Enumerated(EnumType.STRING)
    public TaskType getType() {
        return type;
    }

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    public Project getProject() {
        return project;
    }

    @ManyToOne
    public Assignee getAssigned() {
        return assigned;
    }



    public void setTaskId(Long id) {
        this.taskId = id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setAssigned(Assignee assigned) {
        this.assigned = assigned;
    }
}
