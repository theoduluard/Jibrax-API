package com.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.util.Date;

@Entity
public class Project {

    private Long projectId;

    private String projectName;

    private String projectDescription;

    private Date projectStartDate;

    private Assignee projectOwner;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Date getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Assignee getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(Assignee projectOwner) {
        this.projectOwner = projectOwner;
    }
}
