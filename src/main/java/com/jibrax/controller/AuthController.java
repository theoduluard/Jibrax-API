package com.jibrax.controller;

import com.jibrax.dto.user.UserResponseDTO;
import com.jibrax.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody Map<String,String> body) {
        try {
            return ResponseEntity.ok(authService.login(body.get("username"), body.get("password")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<UserResponseDTO>> getPendingUsers() {
        return ResponseEntity.ok(authService.getPendingUsers());
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<String> validateUser(@PathVariable Long id, @RequestBody Map<String,String> body) {
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
