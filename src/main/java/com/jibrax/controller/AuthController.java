package com.jibrax.controller;

import com.jibrax.dto.user.UserResponseDTO;
import com.jibrax.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Endpoints pour la gestion de la connexion et la validation des inscriptions")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Connecter un utilisateur",
            description = "Permet à un utilisateur de s'authentifier avec son nom d'utilisateur et son mot de passe pour obtenir un token de session."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Identifiants de l'utilisateur",
            required = true,
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(
                            name = "Exemple de login",
                            summary = "Utilisateur standard",
                            value = "{\n  \"username\": \"johndoe\",\n  \"password\": \"monMotDePasse123!\"\n}"
                    )
            })
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = "{\n  \"access_token\": \"eyJhbGciOiJIUzI1...\",\n  \"expires_in\": 3600\n}")
                    })
            ),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = "{\n  \"error\": \"Invalid credentials\"\n}")
                    })
            )
    })
    public ResponseEntity<Map<String,Object>> login(@RequestBody Map<String,String> body) {
        try {
            return ResponseEntity.ok(authService.login(body.get("username"), body.get("password")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }

    @GetMapping("/pending")
    @Operation(
            summary = "Lister les utilisateurs en attente",
            description = "Récupère la liste de tous les utilisateurs qui se sont inscrits mais dont le compte n'a pas encore été validé par un administrateur."
    )
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public ResponseEntity<List<UserResponseDTO>> getPendingUsers() {
        return ResponseEntity.ok(authService.getPendingUsers());
    }

    @PostMapping("/{id}/validate")
    @Operation(
            summary = "Valider un utilisateur",
            description = "Permet de valider l'inscription d'un utilisateur en attente et de lui assigner un rôle définitif (ex: ADMIN, USER)."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Le rôle à assigner à l'utilisateur",
            required = true,
            content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(
                            name = "Exemple de validation",
                            summary = "Assignation du rôle USER",
                            value = "{\n  \"role\": \"USER\"\n}"
                    )
            })
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur validé avec succès",
                    content = @Content(mediaType = "text/plain", examples = {
                            @ExampleObject(value = "User validated successfully with role USER")
                    })
            ),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur lors de la validation",
                    content = @Content(mediaType = "text/plain", examples = {
                            @ExampleObject(value = "Error while validating user: Utilisateur introuvable")
                    })
            )
    })
    public ResponseEntity<String> validateUser(
            @Parameter(description = "L'ID de l'utilisateur à valider", example = "42") @PathVariable Long id,
            @RequestBody Map<String,String> body) {
        try{
            authService.validateUser(id, body.get("role"));
            return ResponseEntity.ok("User validated successfully with role " + body.get("role"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while validating user:" + e.getMessage());
        }
    }
}