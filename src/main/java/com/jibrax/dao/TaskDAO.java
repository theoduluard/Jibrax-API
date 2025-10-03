package com.jibrax.dao;

import com.jibrax.domain.assignee.Assignee;
import com.jibrax.domain.assignee.IAssignee;
import com.jibrax.domain.project.IProject;
import com.jibrax.domain.project.Project;
import com.jibrax.domain.task.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface TaskDAO extends JpaRepository<Task, Long> {

    Task findByTaskId(long id);
    List<Task> findByTaskName(String taskName);
    List<Task> findByDescription(String description);
    List<Task> findByPriority(TaskPriority priority);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByType(TaskType type);
    List<Task> findByAssigned(Assignee assigned);
    List<Task> findByProject(Project project);
}
