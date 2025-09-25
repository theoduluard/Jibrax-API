package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Represents a task within a project.
 * <p>
 * A {@code Task} is a unit of work that can be assigned to either an individual {@link User}
 * or a {@link Team} (through the common superclass {@link Assignee}). Each task has a
 * type, a priority, and a status, which define its nature, urgency, and progress.
 * </p>
 *
 * <h2>Attributes</h2>
 * <ul>
 *   <li>{@link #taskId} – the unique identifier of the task (primary key).</li>
 *   <li>{@link #taskName} – the short title or label of the task.</li>
 *   <li>{@link #description} – a detailed description of the task.</li>
 *   <li>{@link #priority} – the {@link TaskPriority} level (e.g., LOW, MEDIUM, HIGH, URGENT).</li>
 *   <li>{@link #status} – the {@link TaskStatus} of the task (e.g., NEW, IN_PROGRESS, DELIVERED, CLOSED).</li>
 *   <li>{@link #type} – the {@link TaskType} that indicates the nature of the task (BUGFIX, NEW_FEATURE, etc.).</li>
 *   <li>{@link #assigned} – the {@link Assignee} responsible for the task (can be a {@link User} or a {@link Team}).</li>
 *   <li>{@link #project} – the {@link Project} to which the task belongs.</li>
 * </ul>
 *
 * <h2>Persistence</h2>
 * <ul>
 *   <li>{@link #taskId} is the primary key, auto-generated.</li>
 *   <li>{@link #priority}, {@link #status}, and {@link #type} are stored as {@code STRING} enums.</li>
 *   <li>{@link #project} is mapped as a {@code @ManyToOne} relation, non-nullable, with a foreign key {@code project_id}.</li>
 *   <li>{@link #assigned} is mapped as a {@code @ManyToOne} relation to {@link Assignee}.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>
 *     Task task = new Task();
 *     task.setTaskName("Fix login bug");
 *     task.setDescription("Resolve the issue preventing users from logging in.");
 *     task.setPriority(TaskPriority.HIGH);
 *     task.setStatus(TaskStatus.NEW);
 *     task.setType(TaskType.BUGFIX);
 *     task.setAssigned(user);   // or team
 *     task.setProject(project);
 * </pre>
 */
@Entity
@Table(name = "Tasks")
public class Task implements Serializable {

    private Long taskId;
    private String taskName;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private TaskType type;
    private Assignee assigned;
    private Project project;

    /**
     * Returns the unique identifier of the task.
     *
     * @return the task ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getTaskId() {
        return taskId;
    }

    /**
     * Returns the short name of the task.
     *
     * @return the task name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Returns the detailed description of the task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the priority of the task.
     *
     * @return the task priority
     */
    @Enumerated(EnumType.STRING)
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * Returns the current status of the task.
     *
     * @return the task status
     */
    @Enumerated(EnumType.STRING)
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Returns the type of the task.
     *
     * @return the task type
     */
    @Enumerated(EnumType.STRING)
    public TaskType getType() {
        return type;
    }

    /**
     * Returns the project associated with the task.
     *
     * @return the project
     */
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    public Project getProject() {
        return project;
    }

    /**
     * Returns the assignee responsible for the task.
     *
     * @return the assignee (user or team)
     */
    @ManyToOne
    @JsonIgnore
    public Assignee getAssigned() {
        return assigned;
    }

    // --- Setters ---

    /**
     * Sets the unique identifier of the task.
     *
     * @param id the task ID
     */
    public void setTaskId(Long id) {
        this.taskId = id;
    }

    /**
     * Sets the name of the task.
     *
     * @param taskName the name to assign
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Sets the description of the task.
     *
     * @param description the description to assign
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the priority of the task.
     *
     * @param priority the priority level
     */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    /**
     * Sets the status of the task.
     *
     * @param status the status to assign
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Sets the type of the task.
     *
     * @param type the type to assign
     */
    public void setType(TaskType type) {
        this.type = type;
    }

    /**
     * Sets the project associated with the task.
     *
     * @param project the project to assign
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Sets the assignee responsible for the task.
     *
     * @param assigned the assignee (user or team)
     */
    public void setAssigned(Assignee assigned) {
        this.assigned = assigned;
    }
}