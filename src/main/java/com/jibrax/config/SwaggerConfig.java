package com.jibrax.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Jibrax API",
                version = "1.0",
                description = """
      API de gestion de projets, équipes et tâches.

      ## 🔐 API Permissions

      | Endpoint | Method | 🔓 Public | 👤 USER | 👔 MANAGER | 👑 ADMIN | Description |
      |---|---|---|---|---|---|---|
      | **Documentation & Health** | | | | | | |
      | `/swagger-ui/**` | ALL | 🔓 | ✅ | ✅ | ✅ | Swagger UI |
      | `/v3/api-docs/**` | ALL | 🔓 | ✅ | ✅ | ✅ | OpenAPI docs |
      | `/actuator/health` | GET | 🔓 | ✅ | ✅ | ✅ | Health check |
      | **Authentication** | | | | | | |
      | `/api/auth/login` | POST | 🔓 | ✅ | ✅ | ✅ | Login |
      | `/api/auth/pending` | GET | ❌ | ❌ | ❌ | ✅ | Pending accounts |
      | `/api/auth/{id}/validate` | POST | ❌ | ❌ | ❌ | ✅ | Validate account |
      | **Users** | | | | | | |
      | `/api/users` | POST | 🔓 | ✅ | ✅ | ✅ | Registration |
      | `/api/users/me` | GET | ❌ | ✅ | ✅ | ✅ | Own profile |
      | `/api/users/**` | GET | ❌ | ✅ | ✅ | ✅ | Retrieve users |
      | `/api/users/**` | PUT | ❌ | ❌ | ✅ | ✅ | Update user |
      | `/api/users/**` | DELETE | ❌ | ❌ | ❌ | ✅ | Delete user |
      | **Teams** | | | | | | |
      | `/api/teams/**` | GET | ❌ | ✅ | ✅ | ✅ | Retrieve teams |
      | `/api/teams` | POST | ❌ | ❌ | ✅ | ✅ | Create team |
      | `/api/teams/**` | PUT | ❌ | ❌ | ✅ | ✅ | Update team |
      | `/api/teams/**` | DELETE | ❌ | ❌ | ❌ | ✅ | Delete team |
      | **Projects** | | | | | | |
      | `/api/projects/**` | GET | ❌ | ✅ | ✅ | ✅ | Retrieve projects |
      | `/api/projects` | POST | ❌ | ❌ | ✅ | ✅ | Create project |
      | `/api/projects/**` | PUT | ❌ | ❌ | ✅ | ✅ | Update project |
      | `/api/projects/**` | DELETE | ❌ | ❌ | ❌ | ✅ | Delete project |
      | **Tasks** | | | | | | |
      | `/api/tasks/**` | GET | ❌ | ✅ | ✅ | ✅ | Retrieve tasks |
      | `/api/tasks` | POST | ❌ | ✅ | ✅ | ✅ | Create task |
      | `/api/tasks/**` | PUT | ❌ | ✅ | ✅ | ✅ | Update task |
      | `/api/tasks/**` | DELETE | ❌ | ❌ | ✅ | ✅ | Delete task |

      ## 👥 Role Hierarchy
      - 👑 **ADMIN** — Full access (read, write, delete on all resources)
      - 👔 **MANAGER** — Manage teams, projects, tasks ; view and edit users
      - 👤 **USER** — View teams, projects, tasks ; create and edit tasks
      """
        )
)
@SecurityScheme(
        name = "bearer-jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Jibrax API")
                        .version("1.0")
                        .description("Project management API with Keycloak authentication")
                        .contact(new Contact()
                                .name("Jibrax Team")
                                .email("theo.duluard7@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}