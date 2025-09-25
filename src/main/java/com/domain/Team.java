package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a team of users that can be assigned to tasks or projects.
 * <p>
 * A {@code Team} is a specialized type of {@link Assignee}. It groups together multiple users,
 * designates one of them as the leader, and can itself be assigned as the responsible entity
 * for a task or work item.
 * </p>
 *
 * <h2>Structure</h2>
 * <ul>
 *   <li>{@link #teamMembers} – the collection of users that are part of the team.</li>
 * </ul>
 *
 * <h2>Persistence</h2>
 * <ul>
 *   <li>The team members are mapped as a {@code @OneToMany} relation to {@link User}, where each user references the team they belong to.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>
 *     Team team = new Team();
 *     team.setTeamMembers(List.of(user1, user2, user3));
 * </pre>
 */
@Entity
@Table(name = "Teams")
@DiscriminatorValue("TEAM")
public class Team extends Assignee implements Serializable {

    private Collection<User> teamMembers;

    /**
     * Returns the collection of members in the team.
     *
     * @return the list of {@link User} instances that are part of this team
     */
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    public Collection<User> getTeamMembers() {
        return teamMembers;
    }

    /**
     * Sets the collection of members in the team.
     *
     * @param teamMembers the list of {@link User} instances to associate with this team
     */
    public void setTeamMembers(Collection<User> teamMembers) {
        this.teamMembers = teamMembers;
    }
}