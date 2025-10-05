package com.jibrax.service;

import com.jibrax.dao.TeamDAO;
import com.jibrax.dao.UserDAO;
import com.jibrax.domain.team.Team;
import com.jibrax.domain.user.User;
import com.jibrax.dto.user.CreateUserDTO;
import com.jibrax.dto.user.UserResponseDTO;
import com.jibrax.exception.TeamNotFoundException;
import com.jibrax.exception.UserAlreadyExistsException;
import com.jibrax.exception.UserNotFoundException;
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
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserResponseDTO getUserByUsername(String username) {
        User user = userDAO.findByUsername(username);
        if (user == null) throw new UserNotFoundException("User not found with username: " + username);
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO getUserByEmail(String email) {
        User user = userDAO.findByEmail(email);
        if (user == null) throw new UserNotFoundException("User not found with email: " + email);
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO createUser(CreateUserDTO dto) {
        User user = userMapper.toEntity(dto);

        if (dto.getTeamId() != null) {
            Team team = teamDAO.findById(dto.getTeamId())
                    .orElseThrow(() -> new TeamNotFoundException(dto.getTeamId()));
            user.setTeam(team);
        }
        else {
            user.setTeam(null);
        }

        if (userDAO.existsByUsername(dto.getUsername()) || userDAO.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Username or email already taken");
        }

        return userMapper.toResponseDTO(userDAO.save(user));
    }

    public void deleteUser(Long id) {
        if (!userDAO.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userDAO.deleteById(id);
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
                                .orElseThrow(() -> new TeamNotFoundException(dto.getTeamId()));
                        existing.setTeam(team);
                    }
                    else {
                        existing.setTeam(null);
                    }

                    try{
                        User user = userDAO.save(existing);
                        return userMapper.toResponseDTO(user);
                    }
                    catch (Exception e){
                        throw new UserAlreadyExistsException("Username or email already taken");
                    }
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
