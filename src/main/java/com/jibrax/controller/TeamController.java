package com.jibrax.controller;

import com.jibrax.dto.team.CreateTeamDTO;
import com.jibrax.dto.team.TeamResponseDTO;
import com.jibrax.dto.user.UserResponseDTO;
import com.jibrax.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Équipes", description = "Endpoints pour la gestion des équipes et de leurs membres")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    @Operation(summary = "Lister toutes les équipes", description = "Récupère l'ensemble des équipes enregistrées.")
    @ApiResponse(responseCode = "200", description = "Liste des équipes récupérée")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une équipe par son ID", description = "Récupère les informations générales d'une équipe.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipe trouvée"),
            @ApiResponse(responseCode = "404", description = "Équipe non trouvée")
    })
    public ResponseEntity<TeamResponseDTO> getTeam(
            @Parameter(description = "ID de l'équipe") @PathVariable Long id) {
        TeamResponseDTO teamResponseDTO = teamService.getTeam(id);
        if(teamResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teamResponseDTO);
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "Lister les membres d'une équipe", description = "Récupère la liste des utilisateurs appartenant à une équipe spécifique.")
    @ApiResponse(responseCode = "200", description = "Liste des membres récupérée")
    public ResponseEntity<List<UserResponseDTO>> getTeamMembers(
            @Parameter(description = "ID de l'équipe") @PathVariable Long id) {
        List<UserResponseDTO> members = teamService.getTeamMembers(id);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Rechercher une équipe par son nom", description = "Trouve une équipe en utilisant son nom exact.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipe trouvée"),
            @ApiResponse(responseCode = "404", description = "Aucune équipe trouvée avec ce nom")
    })
    public ResponseEntity<TeamResponseDTO> getTeamByName(
            @Parameter(description = "Nom de l'équipe") @PathVariable String name) {
        TeamResponseDTO teamResponseDTO = teamService.getTeamByName(name);
        if(teamResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teamResponseDTO);
    }

    @PostMapping
    @Operation(summary = "Créer une équipe", description = "Crée une nouvelle équipe.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipe créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<TeamResponseDTO> createTeam(@Valid @RequestBody CreateTeamDTO dto) {
        return ResponseEntity.ok(teamService.createTeam(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une équipe", description = "Modifie le nom ou les informations d'une équipe.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipe mise à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Équipe non trouvée")
    })
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @Parameter(description = "ID de l'équipe à modifier") @PathVariable Long id,
            @Valid @RequestBody CreateTeamDTO dto) {
        return ResponseEntity.ok(teamService.updateTeam(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une équipe", description = "Supprime une équipe du système.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipe supprimée"),
            @ApiResponse(responseCode = "404", description = "Équipe non trouvée")
    })
    public ResponseEntity<Void> deleteTeam(
            @Parameter(description = "ID de l'équipe à supprimer") @PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok().build();
    }
}