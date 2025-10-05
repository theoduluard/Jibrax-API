package com.jibrax.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERR_KEY = "error";
    private static final String MSG_KEY = "message";

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse("User not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildResponse("User already exists", ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTeamNotFound(TeamNotFoundException ex) {
        return buildResponse("Team not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleTeamAlreadyExists(TeamAlreadyExistsException ex) {
        return buildResponse("Team already exists", ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProjectNotFound(ProjectNotFoundException ex) {
        return buildResponse("Project not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LeaderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLeaderNotFound(LeaderNotFoundException ex) {
        return buildResponse("Leader not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTaskNotFound(TaskNotFoundException ex) {
        return buildResponse("Task not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AssigneeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAssigneeNotFound(AssigneeNotFoundException ex) {
        return buildResponse("Assignee not found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<Map<String, String>> handleInvalidUserData(InvalidUserDataException ex) {
        return buildResponse("Invalid user data", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return buildResponse("Internal server error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, String>> buildResponse(String error, String message, HttpStatus status) {
        Map<String, String> body = new HashMap<>();
        body.put(ERR_KEY, error);
        body.put(MSG_KEY, message);
        return ResponseEntity.status(status).body(body);
    }
}
