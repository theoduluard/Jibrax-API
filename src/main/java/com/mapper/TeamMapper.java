package com.mapper;

import com.domain.Team;
import com.domain.User;
import com.dto.CreateTeamDTO;
import com.dto.TeamResponseDTO;
import com.dto.TeamMemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamMapper {

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Mapping(source = "assigneeId", target = "id")
    TeamResponseDTO toResponseDTO(Team team);

    @Mapping(target = "assigneeId", ignore = true)
    @Mapping(target = "active", expression = "java(true)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "projectAssigned", ignore = true)
    @Mapping(target = "taskAssigned", ignore = true)
    @Mapping(target = "teamMembers", ignore = true)
    Team toEntity(CreateTeamDTO createTeamDTO);

    @Mapping(source = "assigneeId", target = "id")
    TeamMemberDTO toTeamMemberDTO(User user);
}