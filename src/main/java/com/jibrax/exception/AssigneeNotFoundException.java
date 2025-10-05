package com.jibrax.exception;

public class AssigneeNotFoundException extends RuntimeException {
    public AssigneeNotFoundException(Long id) {
        super("Assignee not found with ID: " + id);
    }
}
