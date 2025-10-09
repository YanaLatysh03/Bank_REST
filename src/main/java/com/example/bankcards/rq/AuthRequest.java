package com.example.bankcards.rq;

public record AuthRequest(
        String email,
        String password
) {
}
