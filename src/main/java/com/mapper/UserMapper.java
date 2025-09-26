package com.mapper;

import com.domain.User;
import com.domain.Team;
import com.dto.CreateUserDTO;
import com.dto.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "assigneeId", target = "id")
    @Mapping(source = "team.username", target = "teamName")
    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "assigneeId", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "projectAssigned", ignore = true)
    @Mapping(target = "taskAssigned", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(source = "teamId", target = "team", qualifiedByName = "teamIdToTeam")
    User toEntity(CreateUserDTO userDTO);

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
