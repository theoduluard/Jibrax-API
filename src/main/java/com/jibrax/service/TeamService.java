package com.jibrax.service;

import com.jibrax.dao.TeamDAO;
import com.jibrax.domain.team.Team;
import com.jibrax.dto.team.CreateTeamDTO;
import com.jibrax.dto.team.TeamResponseDTO;
import com.jibrax.dto.user.UserResponseDTO;
import com.jibrax.exception.TaskNotFoundException;
import com.jibrax.exception.TeamAlreadyExistsException;
import com.jibrax.exception.TeamNotFoundException;
import com.jibrax.exception.UserAlreadyExistsException;
import com.jibrax.mapper.TeamMapper;
import com.jibrax.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private UserMapper userMapper;

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

    public TeamResponseDTO getTeamByName(String name) {
        return teamMapper.toResponseDTO(teamDAO.findByUsername(name));
    }

    public List<UserResponseDTO> getTeamMembers(long teamId) {
        Team team = teamDAO.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        return team.getMembers().stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    public TeamResponseDTO createTeam(CreateTeamDTO dto) {
        Team team = teamMapper.toEntity(dto);
        return teamMapper.toResponseDTO(teamDAO.save(team));
    }

    public TeamResponseDTO updateTeam(long id, CreateTeamDTO dto) {
        if (teamDAO.existsByUsername(dto.getTeamname())) {
            throw new TeamAlreadyExistsException("Team name already taken");
        }

        return teamDAO.findById(id)
                .map(existing -> {
                    existing.setUsername(dto.getTeamname());
                    existing.setImage(dto.getImage());

                    return teamMapper.toResponseDTO(teamDAO.save(existing));
                })
                .orElseThrow(() -> new TeamNotFoundException(id));
    }

    public void deleteTeam(long id) {
        if (!teamDAO.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        teamDAO.deleteById(id);
    }
}
