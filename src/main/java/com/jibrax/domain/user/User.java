package com.jibrax.domain.user;

import com.jibrax.domain.assignee.Assignee;
import com.jibrax.domain.team.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Table(name = "Users")
@DiscriminatorValue("USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends Assignee {

    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private LocalDateTime lastLogin;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

}