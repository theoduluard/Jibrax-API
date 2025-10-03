package com.jibrax.controller;

import com.jibrax.dto.CreateUserDTO;
import com.jibrax.dto.UserResponseDTO;
import com.jibrax.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET all
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = userService.getUser(id);
        if(userResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userResponseDTO);
    }

    // POST create
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    // PUT update
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody CreateUserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
