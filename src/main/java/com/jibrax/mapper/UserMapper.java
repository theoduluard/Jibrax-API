package com.jibrax.mapper;

import com.jibrax.domain.team.Team;
import com.jibrax.domain.user.User;
import com.jibrax.dto.user.CreateUserDTO;
import com.jibrax.dto.user.UserResponseDTO;
import com.jibrax.dto.user.UserTeamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "assigneeId", target = "id")
    @Mapping(source = "team", target = "team", qualifiedByName = "teamToUserTeamDTO")
    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "assigneeId", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "projectsAssigned", ignore = true)
    @Mapping(target = "tasksAssigned", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(source = "teamId", target = "team", qualifiedByName = "teamIdToTeam")
    User toEntity(CreateUserDTO userDTO);

    @Named("teamToUserTeamDTO")
    default UserTeamDTO teamToUserTeamDTO(Team team) {
        if (team == null) {
            return null;
        }
        UserTeamDTO dto = new UserTeamDTO();
        dto.setId(team.getAssigneeId());
        dto.setTeamname(team.getUsername());
        dto.setImage(team.getImage());
        return dto;
    }

    @Named("teamIdToTeam")
    default Team teamIdToTeam(Long teamId) {
        if (teamId == null) {
            return null;
        }
        Team team = new Team();
        team.setAssigneeId(teamId);
        return team;
    }
}