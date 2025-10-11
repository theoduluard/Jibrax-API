package com.jibrax.domain.user;

import com.jibrax.domain.assignee.Assignee;
import com.jibrax.domain.team.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Table(name = "Users")
@DiscriminatorValue("USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends Assignee {

    @Column(unique = true, nullable = false)
    private String email;
    private String firstname;
    private String lastname;
    private LocalDateTime lastLogin;
    private boolean validated = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Team team;

}