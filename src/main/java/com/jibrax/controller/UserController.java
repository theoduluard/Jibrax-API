package com.jibrax.controller;

import com.jibrax.dto.user.CreateUserDTO;
import com.jibrax.dto.user.UserResponseDTO;
import com.jibrax.exception.UserNotFoundException;
import com.jibrax.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateurs", description = "Endpoints pour la gestion du cycle de vie des utilisateurs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Lister tous les utilisateurs", description = "Récupère la liste complète des utilisateurs enregistrés.")
    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me")
    @Operation(summary = "Obtenir l'utilisateur connecté", description = "Récupère les informations de l'utilisateur actuellement authentifié via son token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Informations de l'utilisateur récupérées"),
            @ApiResponse(responseCode = "401", description = "Non authentifié (token manquant ou invalide)"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé en base")
    })
    public ResponseEntity<UserResponseDTO> getCurrentUser(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt) {
        // @Parameter(hidden = true) permet de cacher le paramètre JWT dans Swagger
        // (Swagger gère l'auth via le header Authorization, pas comme un champ de formulaire)
        String email = jwt.getClaimAsString("email");
        UserResponseDTO currentUser = userService.getUserByEmail(email);
        if(currentUser == null) {
            throw new UserNotFoundException(email);
        }
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un utilisateur par son ID", description = "Récupère le profil d'un utilisateur spécifique.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<UserResponseDTO> getUser(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id) {
        UserResponseDTO userResponseDTO = userService.getUser(id);
        if(userResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Rechercher un utilisateur par pseudo", description = "Trouve un utilisateur grâce à son nom d'utilisateur (username).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<UserResponseDTO> getUserByUsername(
            @Parameter(description = "Nom d'utilisateur") @PathVariable String username) {
        UserResponseDTO user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Rechercher un utilisateur par email", description = "Trouve un utilisateur grâce à son adresse email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<UserResponseDTO> getUserByEmail(
            @Parameter(description = "Adresse email de l'utilisateur") @PathVariable String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "Créer un utilisateur", description = "Inscrit un nouvel utilisateur dans le système (généralement via Keycloak).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou email/username déjà pris")
    })
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un utilisateur", description = "Modifie le profil d'un utilisateur existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID de l'utilisateur à modifier") @PathVariable Long id,
            @Valid @RequestBody CreateUserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur", description = "Supprime définitivement un compte utilisateur.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur supprimé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID de l'utilisateur à supprimer") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}