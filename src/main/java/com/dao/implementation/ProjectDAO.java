package com.dao.implementation;

import com.dao.AbstractJpaDao;
import com.domain.Assignee;
import com.domain.Project;
import com.domain.Task;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ProjectDAO extends AbstractJpaDao<Long, Project> {
  public ProjectDAO() {
    super(Project.class);
  }


  public Project findByProjectId(long id){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectId = :id", clazz)
        .setParameter("id", id)
        .getResultList()
        .getFirst();
  }

  public List<Project> findByProjectName(String projectName){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectName = :projectName",clazz)
        .setParameter("projectName",projectName)
        .getResultList();
  }

  public List<Project> findByProjectDescription(String projectDescription){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectDescription = :projectDescription",clazz)
        .setParameter("projectDescription",projectDescription)
        .getResultList();
  }

  public List<Project> findByProjectStartDateBefore(Date projectStartDate){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectStartDate <= :projectStartDate",clazz)
        .setParameter("projectStartDate",projectStartDate)
        .getResultList();
  }

  public List<Project> findByProjectStartDateAfter(Date projectStartDate){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectStartDate >= :projectStartDate",clazz)
        .setParameter("projectStartDate",projectStartDate)
        .getResultList();
  }

  public List<Project> findByProjectStartDateBetween(Date start, Date end){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectStartDate >= :start" +
                "AND e.projectStartDate <= :end",clazz)
        .setParameter("start",start)
        .setParameter("end",end)
        .getResultList();
  }

  public List<Project> findByProjectOwner(Assignee projectOwner){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectOwner >= :projectOwner",clazz)
        .setParameter("projectOwner",projectOwner)
        .getResultList();
  }

  public List<Project> findByTask(Task task){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.tasks LIKE :task",clazz)
        .setParameter("task",task)
        .getResultList();
  }

  public List<Project> findByTask(Collection<Task> tasks){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.tasks LIKE :tasks",clazz)
        .setParameter("tasks",tasks)
        .getResultList();
  }
}
