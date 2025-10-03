package com.jibrax.service;

import com.jibrax.dao.TeamDAO;
import com.jibrax.domain.team.Team;
import com.jibrax.dto.CreateTeamDTO;
import com.jibrax.dto.TeamResponseDTO;
import com.jibrax.mapper.TeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private TeamMapper teamMapper;

    public List<TeamResponseDTO> getAllTeams() {
        return teamDAO.findAll()
                .stream()
                .map(teamMapper::toResponseDTO)
                .toList();
    }

    public TeamResponseDTO getTeam(long id) {
        return teamDAO.findById(id)
                .map(teamMapper::toResponseDTO)
                .orElse(null);
    }

    public TeamResponseDTO createTeam(CreateTeamDTO dto) {
        Team team = teamMapper.toEntity(dto);
        return teamMapper.toResponseDTO(teamDAO.save(team));
    }

    public TeamResponseDTO updateTeam(long id, CreateTeamDTO dto) {
        return teamDAO.findById(id)
                .map(existing -> {
                    existing.setUsername(dto.getUsername());
                    existing.setImage(dto.getImage());

                    return teamMapper.toResponseDTO(teamDAO.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteTeam(long id) {
        teamDAO.deleteById(id);
    }
}
