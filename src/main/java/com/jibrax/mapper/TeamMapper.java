package com.jibrax.mapper;

import com.jibrax.domain.team.Team;
import com.jibrax.dto.team.CreateTeamDTO;
import com.jibrax.dto.team.TeamResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TeamMapper {

    @Mapping(source = "assigneeId", target = "id")
    @Mapping(source = "username", target = "teamname")
    @Mapping(source = "members", target = "teamMembers")
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


}
