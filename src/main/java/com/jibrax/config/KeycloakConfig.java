package com.jibrax.config;

import lombok.Getter;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class KeycloakConfig {

    @Getter
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Getter
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.admin.client-id:admin-cli}")
    private String adminClientId;

    @Getter
    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Getter
    @Value("${keycloak.client-id}")
    private String clientId;

    @Getter
    private Keycloak keycloak;

    @PostConstruct
    public void initKeycloak() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .clientId(adminClientId)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }
}