package com.rest;

import com.dao.implementation.TeamDAO;
import com.dao.implementation.UserDAO;
import com.dto.CreateUserDTO;
import com.dto.UserResponseDTO;
import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/user")
@Tag(name = "USER")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);
    private final UserService userService;

    public UserResource() {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgresql");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        UserDAO userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);

        TeamDAO teamDAO = new TeamDAO();
        teamDAO.setEntityManager(entityManager);

        this.userService = new UserService(userDAO, teamDAO);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a user by id", description = "Retrieve a specific user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Response getUser(
            @Parameter(description = "ID of the user to fetch", required = true)
            @PathParam("id") int id) {

        UserResponseDTO userResponse = userService.getUserById(id);
        if (userResponse == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Utilisateur non trouvé.\"}")
                    .build();
        }
        return Response.ok(userResponse).build();
    }

    @GET
    @Operation(summary = "Get all users", description = "Retrieve a list of all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)))
    })
    public Response getUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @POST
    @Operation(summary = "Create a new user", description = "Add a new user to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Email already used",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    public Response addUser(
            @Parameter(description = "User data for creation", required = true)
            @Valid CreateUserDTO createUserDTO) {

        try {
            UserResponseDTO createdUser = userService.createUser(createUserDTO);
            return Response.status(Response.Status.CREATED).entity(createdUser).build();

        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException
                    || e.getMessage().contains("email")) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\":\"Cet email est déjà utilisé.\"}")
                        .build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Erreur interne.\"}")
                    .build();
        }
    }
}
