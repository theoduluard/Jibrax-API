package com.jibrax.domain.team;

import com.jibrax.domain.assignee.Assignee;
import com.jibrax.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Table(name = "Teams")
@DiscriminatorValue("TEAM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Team extends Assignee {

    @OneToMany(mappedBy = "team")
    private List<User> members = new ArrayList<>();

}