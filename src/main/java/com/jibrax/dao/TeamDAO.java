package com.jibrax.dao;

import com.jibrax.domain.team.Team;
import com.jibrax.domain.user.User;
import jakarta.transaction.Transactional;

@Transactional
public interface TeamDAO extends AssigneeDAO<Team> {
    Team findByMembersContains(User member);
}
