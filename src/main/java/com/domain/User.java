package com.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a user in the system.
 * <p>
 * A {@code User} is a specialization of {@link Assignee} that can be assigned to tasks
 * individually or as part of a {@link Team}. Each user has authentication credentials,
 * personal information, and role-related attributes such as administrator rights.
 * </p>
 *
 * <h2>Attributes</h2>
 * <ul>
 *   <li>{@link #email} – the unique email address of the user, used as an identifier for login.</li>
 *   <li>{@link #password} – the hashed password of the user, used for authentication.</li>
 *   <li>{@link #firstname} – the given name of the user.</li>
 *   <li>{@link #lastname} – the family name of the user.</li>
 *   <li>{@link #lastLogin} – the date and time of the last successful login.</li>
 *   <li>{@link #team} – the team to which the user belongs.</li>
 *   <li>{@link #leadingTeam} – the team for which the user is the designated leader, if any.</li>
 * </ul>
 *
 * <h2>Persistence</h2>
 * <ul>
 *   <li>{@link #team} is mapped as a {@code @ManyToOne} relationship, with the foreign key {@code teamId}.</li>
 *   <li>{@link #leadingTeam} is mapped as a {@code @OneToOne} inverse relation to {@link Team#getTeamLeader()}.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>
 *     User user = new User();
 *     user.setEmail("john.doe@example.com");
 *     user.setFirstname("John");
 *     user.setLastname("Doe");
 *     user.setAdmin(true);
 * </pre>
 */
@Entity
@Table(name = "Users")
@DiscriminatorValue("USER")
public class User extends Assignee implements Serializable {

    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private LocalDateTime lastLogin;
    private Team team;
    private Team leadingTeam;

    /**
     * Returns the email address of the user.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the password of the user.
     * <p>
     * ⚠️ This should normally be stored and returned as a secure hash.
     * </p>
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the first name of the user.
     *
     * @return the user's first name
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the user's last name
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Returns the timestamp of the last login of the user.
     *
     * @return the last login date and time
     */
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    /**
     * Returns the team to which the user belongs.
     *
     * @return the {@link Team} of this user
     */
    @ManyToOne
    @JoinColumn(name = "teamId", nullable = true)
    public Team getTeam() {
        return team;
    }

    /**
     * Returns the team for which the user is designated as leader.
     *
     * @return the {@link Team} led by this user
     */
    @OneToOne(mappedBy = "teamLeader")
    public Team getLeadingTeam() {
        return leadingTeam;
    }

    // --- Setters ---

    /**
     * Sets the email address of the user.
     *
     * @param email the email to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the hashed password to assign
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstname the first name to assign
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastname the last name to assign
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Sets the timestamp of the last login of the user.
     *
     * @param lastLogin the date and time of last login
     */
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Sets the team to which the user belongs.
     *
     * @param team the {@link Team} to associate
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Sets the team for which the user is the designated leader.
     *
     * @param leadingTeam the {@link Team} to associate as led by this user
     */
    public void setLeadingTeam(Team leadingTeam) {
        this.leadingTeam = leadingTeam;
    }
}