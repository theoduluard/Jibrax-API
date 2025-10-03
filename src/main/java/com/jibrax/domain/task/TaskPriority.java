package com.jibrax.domain.task;

/**
 * Represents the priority level of a task.
 * <p>
 * The priority level indicates the relative importance and urgency of a task.
 * It can be used to help in planning, sorting, and resource allocation.
 * </p>
 *
 * <ul>
 *   <li>{@link #LOW} – A task with minimal priority. It can be addressed later and has no immediate impact.</li>
 *   <li>{@link #MEDIUM} – A task of normal importance. It should be handled in due course, but it is not critical.</li>
 *   <li>{@link #HIGH} – A task of significant importance. It should be prioritized and resolved before lower-priority tasks.</li>
 *   <li>{@link #URGENT} – A task requiring immediate attention and delivery, often blocking other work or related to critical issues.</li>
 * </ul>
 */
public enum TaskPriority {

    /**
     * A task with minimal priority. It can be addressed later and has no immediate impact.
     */
    LOW,

    /**
     * A task of normal importance. It should be handled in due course.
     */
    MEDIUM,

    /**
     * A task of significant importance. It should be prioritized over lower-priority tasks.
     */
    HIGH,

    /**
     * A task requiring immediate attention and delivery.
     */
    URGENT
}