package com.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "Projects")
public class Project {

    private Long projectId;

    private String projectName;

    private String projectDescription;

    private Date projectStartDate;

    private Assignee projectLeader;

    private Collection<Task> tasks;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public Date getProjectStartDate() {
        return projectStartDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Assignee getProjectLeader() {
        return projectLeader;
    }

    @OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.LAZY, mappedBy = "project", orphanRemoval = true)
    public Collection<Task> getTasks() {
        return tasks;
    }



    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public void setProjectOwner(Assignee projectOwner) {
        this.projectLeader = projectOwner;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }
}
