package com.dao.implementation;

import com.dao.AbstractJpaDao;
import jakarta.persistence.TypedQuery;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AssigneeDAO<T extends Serializable> extends AbstractJpaDao<Long, T> {
  public AssigneeDAO(Class<T> clazz) {
    super(clazz);
  }

  public T findByAssigneeId(long id){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.assigneeId = :id", clazz)
        .setParameter("id", id)
        .getResultList()
        .getFirst();
  }

  /**
   *
   * @param keyValue a map built like:
   *                 <ul>
   *                 <li>key: the column name in the Table</li>
   *                 <li>value: the value we want to test for the column key</li>
   *                 </ul>
   *
   * @return a list of assignee where each key = value
   */
  public List<T> findByEquality(Map<String, Objects> keyValue) {
    String template = "SELECT e FROM "+ clazz.getSimpleName() + " e";
    boolean isWhere = true;
    for(String key :keyValue.keySet()){
      if(isWhere){
        template += " WHERE e." + key + "= :" + key;
        isWhere = false;
      }else{
        template += " AND e." + key + "= :" + key;
      }
    }
    TypedQuery<T> query = entityManager.createQuery(template, clazz);
    for(String key :keyValue.keySet()) {
      query = query.setParameter(key,keyValue.get(key));
    }
    return query.getResultList();
  }

  public List<T> findByUsername(String username) {
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() + " e WHERE e.username = :username", clazz)
        .setParameter("username", username)
        .getResultList();
  }

  public List<T> findByIsActive(boolean isActive){
    return  entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.isActive = :isActive", clazz)
        .setParameter("isActive", isActive)
        .getResultList();
  }

  public List<T> findCreateAfterDate(LocalDateTime createdAt){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.createdAt >= :createdAt", clazz)
        .setParameter("createdAt", createdAt)
        .getResultList();
  }

  public List<T> findCreateBeforeDate(LocalDateTime createdAt){
    return entityManager.createQuery(
        "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.createdAt <= :createdAt",clazz)
        .setParameter("createdAt", createdAt)
        .getResultList();
  }

  public List<T> findCreateBetweenDates(LocalDateTime start, LocalDateTime end){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.createdAt <= :start AND e.createdAt>= :end",clazz)
        .setParameter("start", start)
        .setParameter("end", end)
        .getResultList();
  }
  public List<T> findUpdateAfterDate(LocalDateTime updatedAt){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.updatedAt >= :updatedAt", clazz)
        .setParameter("createdAt", updatedAt)
        .getResultList();
  }
  public List<T> findUpdateBeforeDate(LocalDateTime updatedAt){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.updatedAt <= :updatedAt", clazz)
        .setParameter("createdAt", updatedAt)
        .getResultList();
  }
  public List<T> findUpdateBetweenDates(LocalDateTime start, LocalDateTime end){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.updatedAt <= :start AND e.updatedAt>= :end",clazz)
        .setParameter("start", start)
        .setParameter("end", end)
        .getResultList();
  }
}
