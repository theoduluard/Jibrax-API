package com.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;


@Entity
@Table(name = "Assignees")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Assignee implements Serializable {

    protected Long assigneeId;

    protected String username;

    protected byte[] image;

    protected boolean isActive;

    protected LocalDateTime createdAt;

    protected LocalDateTime updatedAt;

    private Collection<Project> projectsAssigned;

    private Collection<Task> tasksAssigned;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getAssigneeId() {
        return assigneeId;
    }

    public String getUsername() {
        return username;
    }

    @Lob
    public byte[] getImage() {
        return image;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    @OneToMany(mappedBy = "projectLeader")
    public Collection<Project> getProjectAssigned() {
        return projectsAssigned;
    }

    @OneToMany(mappedBy = "assigned")
    public Collection<Task> getTaskAssigned() {
        return tasksAssigned;
    }



    public void setUsername(String username) {
        this.username = username;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setProjectAssigned(Collection<Project> projectsAssigned) {
        this.projectsAssigned = projectsAssigned;
    }

    public void setTaskAssigned(Collection<Task> tasksAssigned) {
        this.tasksAssigned = tasksAssigned;
    }
}
