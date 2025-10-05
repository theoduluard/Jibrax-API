package com.jibrax.domain.assignee;

import com.jibrax.domain.project.Project;
import com.jibrax.domain.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "Assignees")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Assignee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long assigneeId;
    @Column(unique = true, nullable = false)
    protected String username;  //User: username, Team: name of the team
    protected byte[] image;
    protected boolean active;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    @OneToMany(mappedBy = "projectLeader")
    private Collection<Project> projectsAssigned;

    @OneToMany(mappedBy = "assigned")
    private Collection<Task> tasksAssigned;


    /**
     * Fills createAt and updatedAt fields when the assignee is created.
     */
    @PrePersist
    protected void onCreate() {
        active = true;
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
}
