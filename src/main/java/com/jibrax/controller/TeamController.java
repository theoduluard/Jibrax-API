package com.jibrax.controller;

import com.jibrax.dto.team.CreateTeamDTO;
import com.jibrax.dto.team.TeamResponseDTO;
import com.jibrax.dto.user.UserResponseDTO;
import com.jibrax.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> getTeam(@PathVariable Long id) {
        TeamResponseDTO teamResponseDTO = teamService.getTeam(id);
        if(teamResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teamResponseDTO);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<UserResponseDTO>> getTeamMembers(@PathVariable Long id) {
        List<UserResponseDTO> members = teamService.getTeamMembers(id);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<TeamResponseDTO>  getTeamByName(@PathVariable String name) {
        TeamResponseDTO teamResponseDTO = teamService.getTeamByName(name);
        if(teamResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teamResponseDTO);
    }

    @PostMapping
    public ResponseEntity<TeamResponseDTO> createTeam(@Valid @RequestBody CreateTeamDTO dto) {
        return ResponseEntity.ok(teamService.createTeam(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> updateTeam(@PathVariable Long id, @Valid @RequestBody CreateTeamDTO dto) {
        return ResponseEntity.ok(teamService.updateTeam(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok().build();
    }
}
