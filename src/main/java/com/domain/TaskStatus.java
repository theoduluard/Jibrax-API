package com.domain;

/**
 * Represents the current status of a task.
 * <p>
 * The status indicates the stage of a task in its lifecycle, from creation to closure.
 * </p>
 *
 * <ul>
 *   <li>{@link #NEW} – The task has been created but no work has started yet.</li>
 *   <li>{@link #IN_PROGRESS} – The task is currently being worked on.</li>
 *   <li>{@link #DELIVERED} – The task has been completed and delivered, but may still require validation or review.</li>
 *   <li>{@link #CLOSED} – The task is finalized and no further action is required.</li>
 * </ul>
 */
public enum TaskStatus {

    /**
     * The task has been created but no work has started yet.
     */
    NEW,

    /**
     * The task is currently being worked on.
     */
    IN_PROGRESS,

    /**
     * The task has been completed and delivered, but may still require validation or review.
     */
    DELIVERED,

    /**
     * The task is finalized and no further action is required.
     */
    CLOSED
}