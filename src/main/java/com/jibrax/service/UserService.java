package com.jibrax.service;

import com.jibrax.dao.TeamDAO;
import com.jibrax.dao.UserDAO;
import com.jibrax.domain.team.Team;
import com.jibrax.domain.user.User;
import com.jibrax.dto.CreateUserDTO;
import com.jibrax.dto.UserResponseDTO;
import com.jibrax.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private UserMapper userMapper;

    public List<UserResponseDTO> getAllUsers() {
        return userDAO.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    public UserResponseDTO getUser(Long id) {
        return userDAO.findById(id)
                .map(userMapper::toResponseDTO)
                .orElse(null);
    }

    public UserResponseDTO createUser(CreateUserDTO dto) {
        User user = userMapper.toEntity(dto);

        if (dto.getTeamId() != null) {
            Team team = teamDAO.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id " + dto.getTeamId()));
            user.setTeam(team);
        }

        return userMapper.toResponseDTO(userDAO.save(user));
    }

    public UserResponseDTO updateUser(Long id, CreateUserDTO dto) {
        return userDAO.findById(id)
                .map(existing -> {
                    existing.setFirstname(dto.getFirstname());
                    existing.setLastname(dto.getLastname());
                    existing.setUsername(dto.getUsername());
                    existing.setEmail(dto.getEmail());
                    existing.setPassword(dto.getPassword());
                    existing.setImage(dto.getImage());

                    if (dto.getTeamId() != null) {
                        Team team = teamDAO.findById(dto.getTeamId())
                                .orElseThrow(() -> new RuntimeException("Team not found with id " + dto.getTeamId()));
                        existing.setTeam(team);
                    } else {
                        existing.setTeam(null);
                    }

                    return userMapper.toResponseDTO(userDAO.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        userDAO.deleteById(id);
    }
}
