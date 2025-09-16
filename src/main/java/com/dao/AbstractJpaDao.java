package com.dao;


import java.io.Serializable;
import java.util.List;

import com.jpa.EntityManagerHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public abstract class AbstractJpaDao<K, T extends Serializable> implements IGenericDAO<K, T> {

  protected Class<T> clazz;

  protected EntityManager entityManager;

  public AbstractJpaDao(Class<T> clazzToSet) {
    this.entityManager = EntityManagerHelper.getEntityManager();
    this.clazz = clazzToSet;
  }

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public T findOne(K id) {
    return entityManager.find(clazz, id);
  }

  @Override
  public List<T> findAll() {
    return entityManager.createQuery("select e from " + clazz.getName() + " as e",clazz).getResultList();
  }

  @Override
  public void save(T entity) {
    EntityTransaction t = this.entityManager.getTransaction();
    t.begin();
    entityManager.persist(entity);
    t.commit();
  }

  @Override
  public T update(final T entity) {
    EntityTransaction t = this.entityManager.getTransaction();
    t.begin();
    T res = entityManager.merge(entity);
    t.commit();
    return res;
  }

  @Override
  public void delete(T entity) {
    EntityTransaction t = this.entityManager.getTransaction();
    t.begin();
    entityManager.remove(entity);
    t.commit();
  }

  @Override
  public void deleteById(K entityId) {
    T entity = findOne(entityId);
    delete(entity);
  }
}