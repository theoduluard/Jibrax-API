package com.jibrax.service;

import com.jibrax.config.KeycloakConfig;
import com.jibrax.dao.UserDAO;
import com.jibrax.domain.user.User;
import com.jibrax.dto.user.UserResponseDTO;
import com.jibrax.exception.UserNotFoundException;
import com.jibrax.mapper.UserMapper;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final KeycloakConfig keycloakConfig;
    private final UserDAO userDAO;
    private final UserMapper userMapper;

    public AuthService(KeycloakConfig keycloakConfig, UserDAO userDAO, UserMapper userMapper) {
        this.keycloakConfig = keycloakConfig;
        this.userDAO = userDAO;
        this.userMapper = userMapper;
    }

    public List<UserResponseDTO> getPendingUsers() {
        return userDAO.findByValidatedFalse()
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    public void validateUser(Long id, String role) {
        Keycloak keycloak = keycloakConfig.getKeycloak();
        String realm = keycloakConfig.getRealm();

        Optional<User> userOpt = userDAO.findById(id);
        if(userOpt.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        User user = userOpt.get();

        List<UserRepresentation> found = keycloak.realm(realm).users()
                .search(user.getUsername(), true);
        if (found.isEmpty()) {
            throw new UserNotFoundException("User not found in Keycloak with ID: " + id);
        }

        UserRepresentation kcUser = found.getFirst();
        kcUser.setEnabled(true);
        keycloak.realm(realm).users().get(kcUser.getId()).update(kcUser);

        RoleRepresentation roleRepresentation = keycloak.realm(realm)
                .roles()
                .get(role)
                .toRepresentation();

        keycloak.realm(realm).users().get(kcUser.getId())
                .roles()
                .realmLevel()
                .add(List.of(roleRepresentation));

        user.setValidated(true);
        userDAO.save(user);
    }

    public Map<String, Object> login(String username, String password) {
        String clientSecret = keycloakConfig.getClientSecret();
        String clientId = keycloakConfig.getClientId();
        String realm = keycloakConfig.getRealm();
        String serverUrl = keycloakConfig.getServerUrl();

        RestTemplate rest = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("username", username);
        form.add("password", password);

        try {
            return rest.postForObject(
                    serverUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                    form,
                    Map.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }
}
