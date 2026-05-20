package com.jibrax.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Component
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String MANAGER = "MANAGER";

    private final JwtConverter jwtConverter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Swagger/OpenAPI
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/health").permitAll()

                        // Auth endpoints
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/forgot-password").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/pending").hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/auth/{id}/validate").hasRole(ADMIN)

                        // Users endpoints
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole(USER, ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole(ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole(ADMIN)

                        // Teams endpoints
                        .requestMatchers(HttpMethod.GET, "/api/teams/**").hasAnyRole(USER, ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.POST, "/api/teams").hasAnyRole(ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/api/teams/**").hasAnyRole(ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/api/teams/**").hasRole(ADMIN)

                        // Projects endpoints
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").hasAnyRole(USER, ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.POST, "/api/projects").hasAnyRole(ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasAnyRole(ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasRole(ADMIN)

                        // Tasks endpoints
                        .requestMatchers(HttpMethod.GET, "/api/tasks/**").hasAnyRole(USER, ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasAnyRole(USER, ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/**").hasAnyRole(USER, ADMIN, MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasAnyRole(ADMIN, MANAGER)

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter))
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("""
                                {"error":"Unauthorized","message":"Missing or invalid token"}
                            """);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("""
                                {"error":"Forbidden","message":"Access denied"}
                            """);
                        })
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:*",
                "https://jibrax-api.theoduluard.fr"

                ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}