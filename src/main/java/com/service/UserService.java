package com.service;

import com.dao.implementation.UserDAO;
import com.dao.implementation.TeamDAO;
import com.domain.User;
import com.domain.Team;
import com.dto.CreateUserDTO;
import com.dto.UserResponseDTO;
import com.mapper.UserMapper;
import jakarta.persistence.PersistenceException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserDAO userDAO;
    private final TeamDAO teamDAO;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserService(UserDAO userDAO, TeamDAO teamDAO) {
        this.userDAO = userDAO;
        this.teamDAO = teamDAO;
    }

    public UserResponseDTO createUser(CreateUserDTO createDTO) throws PersistenceException {
        User user = userMapper.toEntity(createDTO);

        if (createDTO.getTeamId() != null) {
            Team team = teamDAO.findByAssigneeId(createDTO.getTeamId());
            if (team == null) {
                throw new IllegalArgumentException("L'équipe avec l'ID " + createDTO.getTeamId() + " n'existe pas");
            }
            user.setTeam(team);
        }

        user.setPassword(hashPassword(createDTO.getPassword()));
        userDAO.save(user);

        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO getUserById(int id) {
        User user = userDAO.findByAssigneeId(id);
        if (user == null) {
            return null;
        }
        return userMapper.toResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userDAO.findAll();
        return users.stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hashage du mot de passe", e);
        }
    }
}