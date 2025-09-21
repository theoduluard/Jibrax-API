package com.dao.implementation;

import com.domain.Team;
import com.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public class UserDAO extends AssigneeDAO<User> {
  public UserDAO() {
      super(User.class);
    }

  public List<User> findByEmail(String email) {
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.email = :email", clazz)
            .setParameter("email", email)
            .getResultList();
  }

  public List<User> findByFirstName(String firstname) {
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.firstname = :firstname", clazz)
            .setParameter("firstname", firstname)
            .getResultList();
  }

  public List<User> findByLastName(String lastname) {
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.lastname = :lastname", clazz)
            .setParameter("lastname", lastname)
            .getResultList();
  }

  public List<User> findByFirstNameAndLastName(String firstname,String lastname) {
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.lastname = :lastname " +
                "AND e.firstname = :firstname", clazz)
            .setParameter("firstname", firstname)
            .setParameter("lastname", lastname)
            .getResultList();
  }

  public List<User> findByLogInBefore(LocalDateTime lastlogin) {
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.lastLogin >= :lastlogin ", clazz)
            .setParameter("lastlogin", lastlogin)
            .getResultList();
  }

  public List<User> findByLogInAfter(LocalDateTime lastlogin) {
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.lastLogin <= :lastlogin ", clazz)
            .setParameter("lastlogin", lastlogin)
            .getResultList();
  }

  public List<User> findByLogInBetween(LocalDateTime start, LocalDateTime end) {
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.lastLogin <= :end " +
                "AND e.lastLogin >= :start", clazz)
            .setParameter("start", start)
            .setParameter("end", end)
            .getResultList();
  }

  public List<User> findByTeam(Team team){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.team = :team",clazz)
            .setParameter("team",team)
            .getResultList();
  }

  public User findLeaderByTeam(Team leadingTeam){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.leadingTeam = :leadingTeam",clazz)
            .setParameter("leadingTeam",leadingTeam)
            .getSingleResult();
  }
}