package com.jibrax.controller;

import com.jibrax.dto.CreateTeamDTO;
import com.jibrax.dto.TeamResponseDTO;
import com.jibrax.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

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

    @PostMapping
    public ResponseEntity<TeamResponseDTO> createUser(@Valid @RequestBody CreateTeamDTO dto) {
        return ResponseEntity.ok(teamService.createTeam(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody CreateTeamDTO dto) {
        return ResponseEntity.ok(teamService.updateTeam(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}