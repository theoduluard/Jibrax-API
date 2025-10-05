package com.jibrax.mapper;

import com.jibrax.domain.team.Team;
import com.jibrax.domain.user.User;
import com.jibrax.dto.team.CreateTeamDTO;
import com.jibrax.dto.team.TeamResponseDTO;
import com.jibrax.dto.team.TeamUserDTO;
import java.util.Collections;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(source = "assigneeId", target = "id")
    @Mapping(source = "username", target = "teamname")
    @Mapping(source = "members", target = "teamMembers", qualifiedByName = "membersToTeamUserDTOList")
    TeamResponseDTO toResponseDTO(Team team);

    @Mapping(target = "assigneeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "projectsAssigned", ignore = true)
    @Mapping(target = "tasksAssigned", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(source = "teamname", target = "username")
    Team toEntity(CreateTeamDTO createTeamDTO);

    @Named("membersToTeamUserDTOList")
    default List<TeamUserDTO> membersToTeamUserDTOList(List<User> members) {
        if (members == null) {
            return Collections.emptyList();
        }

        return members.stream()
                .map(this::userToTeamUserDTO)
                .toList();
    }

    @Named("userToTeamUserDTO")
    default TeamUserDTO userToTeamUserDTO(User user) {
        if (user == null) {
            return null;
        }

        TeamUserDTO dto = new TeamUserDTO();
        dto.setId(user.getAssigneeId());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setImage(user.getImage());
        return dto;
    }
}