package com.jibrax.domain.project;

import com.jibrax.domain.assignee.Assignee;
import com.jibrax.domain.assignee.IAssignee;
import com.jibrax.domain.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "Projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String projectName;

    private String projectDescription;

    private Date projectStartDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_leader_id")
    private Assignee projectLeader;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Task> tasks;
}