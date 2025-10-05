package com.jibrax.exception;

public class LeaderNotFoundException extends RuntimeException {
    public LeaderNotFoundException(Long id) {
        super("Leader not found with ID: " + id);
    }
}
