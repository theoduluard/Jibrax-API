package com.jibrax.dao;

import com.jibrax.domain.assignee.Assignee;
import com.jibrax.domain.project.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

@Transactional
public interface ProjectDAO extends JpaRepository<Project, Long> {
    List<Project> findByProjectDescription(String projectDescription);
    List<Project> findByProjectStartDateBefore(Date projectStartDate);
    List<Project> findByProjectStartDateAfter(Date projectStartDate);
    List<Project> findByProjectStartDateBetween(Date start, Date end);
    List<Project> findByProjectLeader(Assignee projectLeader);
}
