package com.service;

import com.dao.implementation.TeamDAO;
import com.dao.implementation.UserDAO;
import com.domain.Team;
import com.domain.User;
import com.dto.CreateTeamDTO;
import com.dto.TeamResponseDTO;
import com.mapper.TeamMapper;
import jakarta.persistence.PersistenceException;

import java.util.List;
import java.util.stream.Collectors;

public class TeamService {

    private final TeamDAO teamDAO;
    private final UserDAO userDAO;
    private final TeamMapper teamMapper = TeamMapper.INSTANCE;

    public TeamService(TeamDAO teamDAO, UserDAO userDAO) {
        this.teamDAO = teamDAO;
        this.userDAO = userDAO;
    }

    public TeamResponseDTO createTeam(CreateTeamDTO createDTO) throws PersistenceException {
        Team team = teamMapper.toEntity(createDTO);
        teamDAO.save(team);
        return teamMapper.toResponseDTO(team);
    }

    public TeamResponseDTO getTeamById(Long id) {
        Team team = teamDAO.findByAssigneeId(id);
        if (team == null) {
            return null;
        }
        return teamMapper.toResponseDTO(team);
    }

    public List<TeamResponseDTO> getAllTeams() {
        List<Team> teams = teamDAO.findAll();
        return teams.stream()
                .map(teamMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TeamResponseDTO addMemberToTeam(Long teamId, Long userId) throws PersistenceException {
        Team team = teamDAO.findByAssigneeId(teamId);
        if (team == null) {
            throw new IllegalArgumentException("L'équipe avec l'ID " + teamId + " n'existe pas");
        }

        User user = userDAO.findByAssigneeId(userId.intValue());
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur avec l'ID " + userId + " n'existe pas");
        }

        user.setTeam(team);
        userDAO.save(user);

        return teamMapper.toResponseDTO(teamDAO.findByAssigneeId(teamId));
    }

    public TeamResponseDTO removeMemberFromTeam(Long teamId, Long userId) throws PersistenceException {
        User user = userDAO.findByAssigneeId(userId.intValue());
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur avec l'ID " + userId + " n'existe pas");
        }

        user.setTeam(null);
        userDAO.save(user);

        Team team = teamDAO.findByAssigneeId(teamId);
        return team != null ? teamMapper.toResponseDTO(team) : null;
    }
}
