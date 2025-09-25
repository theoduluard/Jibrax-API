package com.rest;

import com.dao.implementation.TeamDAO;
import com.dao.implementation.UserDAO;
import com.dto.CreateTeamDTO;
import com.dto.TeamResponseDTO;
import com.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/team")
@Tag(name = "TEAM")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

    private final TeamService teamService;

    public TeamResource() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgresql");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        UserDAO userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);

        TeamDAO teamDAO = new TeamDAO();
        teamDAO.setEntityManager(entityManager);

        this.teamService = new TeamService(teamDAO, userDAO);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a team by id", description = "Retrieve a specific team by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public Response getTeam(
            @Parameter(description = "ID of the team to fetch", required = true)
            @PathParam("id") Long id) {

        TeamResponseDTO teamResponse = teamService.getTeamById(id);
        if (teamResponse == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Équipe non trouvée.\"}")
                    .build();
        }
        return Response.ok(teamResponse).build();
    }

    @GET
    @Operation(summary = "Get all teams", description = "Retrieve a list of all teams.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of teams retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamResponseDTO.class)))
    })
    public Response getTeams() {
        List<TeamResponseDTO> teams = teamService.getAllTeams();
        return Response.ok(teams).build();
    }

    @POST
    @Operation(summary = "Create a new team", description = "Add a new team to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Team created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Team name already used"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response createTeam(
            @Parameter(description = "Team data for creation", required = true)
            @Valid CreateTeamDTO createTeamDTO) {

        try {
            TeamResponseDTO createdTeam = teamService.createTeam(createTeamDTO);
            return Response.status(Response.Status.CREATED).entity(createdTeam).build();

        } catch (PersistenceException e) {
            if (e.getMessage().contains("username") || e.getMessage().contains("unique")) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\":\"Ce nom d'équipe est déjà utilisé.\"}")
                        .build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Erreur interne.\"}")
                    .build();
        }
    }

    @POST
    @Path("/{teamId}/members/{userId}")
    @Operation(summary = "Add member to team", description = "Add a user to a team.")
    public Response addMemberToTeam(
            @PathParam("teamId") Long teamId,
            @PathParam("userId") Long userId) {

        try {
            TeamResponseDTO updatedTeam = teamService.addMemberToTeam(teamId, userId);
            return Response.ok(updatedTeam).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{teamId}/members/{userId}")
    @Operation(summary = "Remove member from team", description = "Remove a user from a team.")
    public Response removeMemberFromTeam(
            @PathParam("teamId") Long teamId,
            @PathParam("userId") Long userId) {

        try {
            TeamResponseDTO updatedTeam = teamService.removeMemberFromTeam(teamId, userId);
            return Response.ok(updatedTeam).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}