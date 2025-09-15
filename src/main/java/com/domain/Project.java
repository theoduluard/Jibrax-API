package com.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * Represents a project in the system.
 * <p>
 * A {@code Project} groups together multiple {@link Task tasks} and is led by
 * an {@link Assignee}, which can be either a {@link User} or a {@link Team}.
 * Each project has a name, description, start date, and a collection of tasks.
 * </p>
 *
 * <h2>Attributes</h2>
 * <ul>
 *   <li>{@link #projectId} – the unique identifier of the project (primary key).</li>
 *   <li>{@link #projectName} – the short title or label of the project.</li>
 *   <li>{@link #projectDescription} – a textual description providing more details about the project.</li>
 *   <li>{@link #projectStartDate} – the starting date of the project.</li>
 *   <li>{@link #projectLeader} – the {@link Assignee} responsible for leading the project.</li>
 *   <li>{@link #tasks} – the collection of {@link Task tasks} associated with this project.</li>
 * </ul>
 *
 * <h2>Persistence</h2>
 * <ul>
 *   <li>{@link #projectId} is the primary key, generated using {@code IDENTITY} strategy.</li>
 *   <li>{@link #projectLeader} is mapped as a {@code @ManyToOne} relation (lazy-loaded).</li>
 *   <li>{@link #tasks} is mapped as a {@code @OneToMany} relation with cascade and orphan removal.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>
 *     Project project = new Project();
 *     project.setProjectName("Website Redesign");
 *     project.setProjectDescription("Complete overhaul of the company website.");
 *     project.setProjectStartDate(new Date());
 *     project.setProjectOwner(user);  // or team
 * </pre>
 */
@Entity
@Table(name = "Projects")
public class Project implements Serializable {

    private Long projectId;
    private String projectName;
    private String projectDescription;
    private Date projectStartDate;
    private Assignee projectLeader;
    private Collection<Task> tasks;

    /**
     * Returns the unique identifier of the project.
     *
     * @return the project ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getProjectId() {
        return projectId;
    }

    /**
     * Returns the name of the project.
     *
     * @return the project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Returns the description of the project.
     *
     * @return the project description
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * Returns the start date of the project.
     *
     * @return the project start date
     */
    public Date getProjectStartDate() {
        return projectStartDate;
    }

    /**
     * Returns the leader of the project.
     *
     * @return the project leader
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Assignee getProjectLeader() {
        return projectLeader;
    }

    /**
     * Returns the collection of tasks associated with the project.
     *
     * @return the list of tasks
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "project", orphanRemoval = true)
    public Collection<Task> getTasks() {
        return tasks;
    }

    // --- Setters ---

    /**
     * Sets the unique identifier of the project.
     *
     * @param projectId the project ID
     */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /**
     * Sets the name of the project.
     *
     * @param projectName the project name
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Sets the description of the project.
     *
     * @param projectDescription the project description
     */
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    /**
     * Sets the start date of the project.
     *
     * @param projectStartDate the project start date
     */
    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    /**
     * Sets the leader of the project.
     *
     * @param projectLeader the project leader
     */
    public void setProjectLeader(Assignee projectLeader) {
        this.projectLeader = projectLeader;
    }

    /**
     * Sets the collection of tasks associated with the project.
     *
     * @param tasks the list of tasks
     */
    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }
}