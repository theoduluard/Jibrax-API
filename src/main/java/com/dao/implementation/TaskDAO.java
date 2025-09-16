package com.dao.implementation;

import com.dao.AbstractJpaDao;
import com.domain.Assignee;
import com.domain.Project;
import com.domain.Task;
import com.domain.TaskPriority;
import com.domain.TaskStatus;
import com.domain.TaskType;

import java.util.List;

public class TaskDAO  extends AbstractJpaDao<Long, Task> {
  public TaskDAO() {
    super(Task.class);
  }


  public Task findByTaskId(long id){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.taskId = :id", clazz)
        .setParameter("id", id)
        .getResultList()
        .getFirst();
  }

  public List<Task> findByTaskName(String taskName){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.taskName = :taskName",clazz)
        .setParameter("taskName",taskName)
        .getResultList();
  }

  public List<Task> findByDescription(String description){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.description = :description",clazz)
        .setParameter("description",description)
        .getResultList();
  }

  public List<Task> findByPriority(TaskPriority priority){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.priority = :priority",clazz)
        .setParameter("priority",priority)
        .getResultList();
  }

  public List<Task> findByStatus(TaskStatus status){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.status = :status",clazz)
        .setParameter("status",status)
        .getResultList();
  }

  public List<Task> findByStatusAndPriority(TaskPriority priority, TaskStatus status){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.status = :status" +
                "AND e.priority = :priority",clazz)
        .setParameter("priority",priority)
        .setParameter("status",status)
        .getResultList();
  }

  public List<Task> findByType(TaskType type){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.type = :type",clazz)
        .setParameter("type",type)
        .getResultList();
  }

  public List<Task> findByTypeAndPriority(TaskPriority priority, TaskType type){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.type = :type" +
                "AND e.priority = :priority",clazz)
        .setParameter("priority",priority)
        .setParameter("type",type)
        .getResultList();
  }

  public List<Task> findByAssignee(Assignee assignedUser){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.assignedUser = :assignedUser",clazz)
        .setParameter("assignedUser",assignedUser)
        .getResultList();
  }

  public List<Task> findByproject(Project project){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.project = :project",clazz)
        .setParameter("project",project)
        .getResultList();
  }
}
