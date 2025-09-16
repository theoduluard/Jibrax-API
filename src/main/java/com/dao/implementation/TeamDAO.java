package com.dao.implementation;

import com.dao.AbstractJpaDao;
import com.domain.Team;
import com.domain.User;

import java.util.List;

public class TeamDAO extends AssigneeDAO<Team> {
  public TeamDAO() {
    super(Team.class);
  }


  public List<Team> findByTeamLeader(User teamleader){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.teamleader = :teamleader",clazz)
        .setParameter("teamleader",teamleader)
        .getResultList();
  }

  public List<Team> findByTeamMembers(List<User> members){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.teamMembers LIKE :t", clazz)
        .setParameter("t", members)
        .setParameter("members",members)
        .getResultList();
  }

  public List<Team> findByMembers(User member){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE e.teamMembers LIKE :t", clazz)
        .setParameter("t", member)
        .setParameter("members",member)
        .getResultList();
  }
}
