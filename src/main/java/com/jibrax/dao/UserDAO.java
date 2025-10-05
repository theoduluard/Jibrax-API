package com.jibrax.dao;

import com.jibrax.domain.user.User;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface UserDAO extends AssigneeDAO<User> {
    User findByEmail(String email);
    List<User> findByFirstname(String firstname);
    List<User> findByLastname(String lastname);
    List<User> findByFirstnameAndLastname(String firstname,String lastname);
    List<User> findByLastLoginIsBefore(LocalDateTime lastlogin);
    List<User> findByLastLoginIsAfter(LocalDateTime lastlogin);
    List<User> findByLastLoginIsBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}