package com.domain;

/**
 * Represents the different types of tasks that can be created in the system.
 * <p>
 * Each task type indicates the purpose or category of the work item:
 * </p>
 *
 * <ul>
 *   <li>{@link #BUGFIX} – A task dedicated to resolving an existing bug or defect in the system.</li>
 *   <li>{@link #NEW_FEATURE} – A task that introduces or implements a new functionality.</li>
 *   <li>{@link #TECHNICAL_ANALYSIS} – A task focused on research, exploration, or analysis of a technical topic
 *       (e.g., investigating feasibility, studying performance issues, or preparing architectural decisions).</li>
 * </ul>
 */
public enum TaskType {

    /**
     * A task dedicated to resolving an existing bug or defect.
     */
    BUGFIX,

    /**
     * A task that introduces or implements a new functionality.
     */
    NEW_FEATURE,

    /**
     * A task focused on research, exploration, or analysis of a technical aspect.
     */
    TECHNICAL_ANALYSIS,

    /**
     * A task focus on updating or creating documentation.
     */
    DOCUMENTATION,
}