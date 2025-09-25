package com.dao.implementation;

import com.domain.Team;
import com.domain.User;

public class TeamDAO extends AssigneeDAO<Team> {
  public TeamDAO() {
    super(Team.class);
  }

  public Team findByMember(User member){
    return entityManager.createQuery(
            "SELECT e FROM "+ clazz.getSimpleName() +" e WHERE :member MEMBER OF e.teamMembers", clazz)
            .setParameter("member", member)
            .getSingleResult();
  }
}
