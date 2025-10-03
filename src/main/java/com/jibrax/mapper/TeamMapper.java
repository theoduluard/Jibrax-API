package com.jibrax.mapper;

import com.jibrax.domain.team.Team;
import com.jibrax.dto.CreateTeamDTO;
import com.jibrax.dto.TeamResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(target = "id", source = "assigneeId")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "image", source = "image")
    TeamResponseDTO toResponseDTO(Team team);

    @Mapping(target = "assigneeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Team toEntity(CreateTeamDTO createTeamDTO);
}
