package com.dao.implementation;

import com.dao.AbstractJpaDao;
import com.domain.Assignee;
import com.domain.Project;
import com.domain.Task;

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
            .getSingleResult();
  }

  public Project findByProjectName(String projectName){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectName = :projectName",clazz)
            .setParameter("projectName", projectName)
            .getSingleResult();
  }

  public List<Project> findByProjectDescription(String projectDescription){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectDescription LIKE :projectDescription",clazz)
            .setParameter("projectDescription", "%"+projectDescription+"%")
            .getResultList();
  }

  public List<Project> findByProjectStartDateBefore(Date projectStartDate){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectStartDate <= :projectStartDate",clazz)
            .setParameter("projectStartDate", projectStartDate)
            .getResultList();
  }

  public List<Project> findByProjectStartDateAfter(Date projectStartDate){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectStartDate >= :projectStartDate",clazz)
            .setParameter("projectStartDate", projectStartDate)
            .getResultList();
  }

  public List<Project> findByProjectStartDateBetween(Date start, Date end){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectStartDate BETWEEN :start AND :end",clazz)
            .setParameter("start", start)
            .setParameter("end", end)
            .getResultList();
  }

  public List<Project> findByProjectLeader(Assignee projectLeader){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.projectLeader = :projectLeader",clazz)
            .setParameter("projectLeader", projectLeader)
            .getResultList();
  }

  public Project findByTask(Task task){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE :task MEMBER OF e.tasks",clazz)
            .setParameter("task", task)
            .getSingleResult();
  }
}
