package com.jibrax.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoRepositoryBean
public interface AssigneeDAO<T extends Serializable> extends JpaRepository<T, Long> {
    T findByUsername(String username);
    List<T> findByActive(boolean active);
    List<T> findByCreatedAtAfter(LocalDateTime createdAt);
    List<T> findByCreatedAtBefore(LocalDateTime createdAt);
    List<T> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<T> findByUpdatedAtAfter(LocalDateTime updatedAt);
    List<T> findByUpdatedAtBefore(LocalDateTime updatedAt);
    List<T> findByUpdatedAtBetween(LocalDateTime start, LocalDateTime end);
}
