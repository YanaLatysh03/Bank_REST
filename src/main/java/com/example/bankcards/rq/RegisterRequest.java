package com.example.bankcards.rq;

public record RegisterRequest(
        String email,
        String password,
        String role
) {
}
