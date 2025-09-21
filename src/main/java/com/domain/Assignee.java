package com.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;


/**
 * Represents an entity that can be assigned to tasks or projects.
 * <p>
 * An {@code Assignee} is an abstract base class for both {@link User} and {@link Team}.
 * It defines common attributes such as identity, username, profile image, and metadata,
 * as well as relationships with assigned {@link Project projects} and {@link Task tasks}.
 * </p>
 *
 * <h2>Inheritance</h2>
 * <ul>
 *   <li>{@link User} – represents an individual person with authentication credentials and roles.</li>
 *   <li>{@link Team} – represents a group of users working together under a team leader.</li>
 * </ul>
 *
 * <h2>Attributes</h2>
 * <ul>
 *   <li>{@link #assigneeId} – the unique identifier of the assignee (primary key).</li>
 *   <li>{@link #username} – a unique username for identifying the assignee.</li>
 *   <li>{@link #image} – a binary representation (BLOB) of the profile picture or logo.</li>
 *   <li>{@link #isActive} – whether the assignee is currently active in the system.</li>
 *   <li>{@link #createdAt} – the timestamp of creation.</li>
 *   <li>{@link #updatedAt} – the timestamp of the last modification.</li>
 *   <li>{@link #projectsAssigned} – the collection of {@link Project projects} led by this assignee.</li>
 *   <li>{@link #tasksAssigned} – the collection of {@link Task tasks} assigned to this assignee.</li>
 * </ul>
 *
 * <h2>Persistence</h2>
 * <ul>
 *   <li>Inheritance uses {@link InheritanceType#JOINED}, so subclasses have their own tables.</li>
 *   <li>{@link #assigneeId} is the primary key, generated using {@code IDENTITY} strategy.</li>
 *   <li>{@link #image} is stored as a {@code @Lob} (BLOB).</li>
 *   <li>{@link #projectsAssigned} is mapped by {@code projectLeader} in {@link Project}.</li>
 *   <li>{@link #tasksAssigned} is mapped by {@code assigned} in {@link Task}.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>
 *     // As a User
 *     User user = new User();
 *     user.setUsername("john.doe");
 *     user.setActive(true);
 *
 *     // As a Team
 *     Team team = new Team();
 *     team.setUsername("Backend Team");
 *     team.setActive(true);
 * </pre>
 */
@Entity
@Table(name = "Assignees")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Assignee implements Serializable {

    protected Long assigneeId;
    protected String username;
    protected byte[] image;
    protected boolean active;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    private Collection<Project> projectsAssigned;
    private Collection<Task> tasksAssigned;


    /**
     * Fills createAt and updatedAt fields when the assignee is created.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if(this.createdAt == null) this.createdAt = now;
        if(this.updatedAt == null) this.updatedAt = now;
    }

    /**
     * Update the updateAt field when the assignee is modified.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Returns the unique identifier of the assignee.
     *
     * @return the assignee ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getAssigneeId() {
        return assigneeId;
    }

    /**
     * Returns the username of the assignee.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the profile image of the assignee.
     *
     * @return the image as a byte array
     */
    @Lob
    public byte[] getImage() {
        return image;
    }

    /**
     * Indicates whether the assignee is active.
     *
     * @return {@code true} if active, otherwise {@code false}
     */
    @Column(nullable = false)
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the timestamp when the assignee was created.
     *
     * @return the creation timestamp
     */
    @Column(nullable = false, updatable = false)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the timestamp when the assignee was last updated.
     *
     * @return the last update timestamp
     */
    @Column(nullable = false)
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns the collection of projects led by this assignee.
     *
     * @return the assigned projects
     */
    @OneToMany(mappedBy = "projectLeader", cascade = CascadeType.DETACH)
    public Collection<Project> getProjectAssigned() {
        return projectsAssigned;
    }

    /**
     * Returns the collection of tasks assigned to this assignee.
     *
     * @return the assigned tasks
     */
    @OneToMany(mappedBy = "assigned", cascade = CascadeType.DETACH)
    public Collection<Task> getTaskAssigned() {
        return tasksAssigned;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setActive(boolean active) {
        this.active = active;
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
