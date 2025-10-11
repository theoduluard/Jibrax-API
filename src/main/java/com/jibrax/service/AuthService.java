package com.jibrax.service;

import com.jibrax.config.KeycloakConfig;
import com.jibrax.dao.UserDAO;
import com.jibrax.domain.user.User;
import com.jibrax.dto.user.UserResponseDTO;
import com.jibrax.exception.UserNotFoundException;
import com.jibrax.mapper.UserMapper;
import org.keycloak.admin.client.Keycloak;
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

    public Boolean validateUser(Long id) {
        Keycloak keycloak = keycloakConfig.getKeycloak();
        String realm = keycloakConfig.getRealm();

        Optional<User> userOpt = userDAO.findById(id);
        if(userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        List<UserRepresentation> found = keycloak.realm(realm).users()
                .search(userOpt.get().getUsername(), true);
        if (found.isEmpty()) {
            return false;
        }

        UserRepresentation kcUser = found.getFirst();
        kcUser.setEnabled(true);
        keycloak.realm(realm).users().get(kcUser.getId()).update(kcUser);

        user.setValidated(true);
        userDAO.save(user);

        keycloak.realm(realm).users().get(kcUser.getId())
                .executeActionsEmail(List.of());

        return true;
    }

    public void sendResetPasswordEmail(String username) {
        Keycloak keycloak = keycloakConfig.getKeycloak();
        String realm = keycloakConfig.getRealm();

        List<UserRepresentation> users = keycloak.realm(realm).users().search(username);
        if (users.isEmpty()) {
            throw new UserNotFoundException("User not found in Keycloak");
        }

        String userId = users.getFirst().getId();

        keycloak.realm(realm)
                .users()
                .get(userId)
                .executeActionsEmail(
                        null,
                        null,
                        3600,
                        List.of("UPDATE_PASSWORD")
                );
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
