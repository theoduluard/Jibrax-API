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
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import com.jibrax.config.KeycloakConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final TeamDAO teamDAO;
    private final UserMapper userMapper;
    private final KeycloakConfig keycloakConfig;

    public UserService(UserDAO userDAO, TeamDAO teamDAO, UserMapper userMapper, KeycloakConfig keycloakConfig) {
        this.userDAO = userDAO;
        this.teamDAO = teamDAO;
        this.userMapper = userMapper;
        this.keycloakConfig = keycloakConfig;
    }

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

    @Transactional
    public UserResponseDTO createUser(CreateUserDTO dto) {
        if (userDAO.existsByUsername(dto.getUsername()) || userDAO.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Username or email already taken");
        }

        User user = userMapper.toEntity(dto);

        if (dto.getTeamId() != null) {
            Team team = teamDAO.findById(dto.getTeamId())
                    .orElseThrow(() -> new TeamNotFoundException(dto.getTeamId()));
            user.setTeam(team);
        }
        User savedUser = userDAO.save(user);

        createKeycloakUser(dto, user.getAssigneeId());

        return userMapper.toResponseDTO(savedUser);
    }

    private void createKeycloakUser(CreateUserDTO dto, Long userId) {
        Keycloak keycloak = keycloakConfig.getKeycloak();
        String realm = keycloakConfig.getRealm();

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.getPassword());
        credential.setTemporary(false);

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(dto.getUsername());
        kcUser.setEmail(dto.getEmail());
        kcUser.setFirstName(dto.getFirstname());
        kcUser.setLastName(dto.getLastname());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(true);
        kcUser.setCredentials(List.of(credential));
        kcUser.setAttributes(Map.of("db_user_id", List.of(userId.toString())));

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource userResource = realmResource.users();

        try (Response response = userResource.create(kcUser)) {
            if (response.getStatus() != 201) {
                throw new RuntimeException("Keycloak user creation failed: " +
                        response.getStatusInfo());
            }
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        try {
            deleteKeycloakUser(user);
        } catch (Exception e) {
            throw new RuntimeException("Warning: Failed to delete user from Keycloak: " + e.getMessage());
        }

        userDAO.deleteById(id);
    }

    private void deleteKeycloakUser(User user) {
        Keycloak keycloak = keycloakConfig.getKeycloak();
        String realm = keycloakConfig.getRealm();

        List<UserRepresentation> found = keycloak.realm(realm).users()
                .search(user.getUsername(), true);

        if (found.isEmpty()) {
            throw new UserNotFoundException("User not found in Keycloak with username: " + user.getUsername());
        }

        UserRepresentation kcUser = found.getFirst();
        try {
            keycloak.realm(realm).users().get(kcUser.getId()).remove();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user from Keycloak: " + e.getMessage(), e);
        }
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, CreateUserDTO dto) {
        User existing = userDAO.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        existing.setFirstname(dto.getFirstname());
        existing.setLastname(dto.getLastname());
        existing.setUsername(dto.getUsername());
        existing.setEmail(dto.getEmail());
        existing.setImage(dto.getImage());

        if (dto.getTeamId() != null) {
            Team team = teamDAO.findById(dto.getTeamId())
                    .orElseThrow(() -> new TeamNotFoundException(dto.getTeamId()));
            existing.setTeam(team);
        }
        else {
            existing.setTeam(null);
        }

        User savedUser;
        try {
            savedUser = userDAO.save(existing);
        }
        catch (Exception e) {
            throw new UserAlreadyExistsException("Username or email already taken");
        }

        try {
            updateKeycloakUser(savedUser);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to update user in Keycloak: " + e.getMessage(), e);
        }

        return userMapper.toResponseDTO(savedUser);
    }

    private void updateKeycloakUser(User user) {
        Keycloak keycloak = keycloakConfig.getKeycloak();
        String realm = keycloakConfig.getRealm();

        List<UserRepresentation> found = keycloak.realm(realm).users()
                .search(user.getUsername(), true);

        if (found.isEmpty()) {
            found = keycloak.realm(realm).users()
                    .searchByAttributes("db_user_id:" + user.getAssigneeId());
        }

        if (found.isEmpty()) {
            throw new UserNotFoundException("User not found in Keycloak");
        }

        UserRepresentation kcUser = found.getFirst();

        kcUser.setUsername(user.getUsername());
        kcUser.setEmail(user.getEmail());
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());

        keycloak.realm(realm).users().get(kcUser.getId()).update(kcUser);
    }
}
