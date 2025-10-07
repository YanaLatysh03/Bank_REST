package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;

public record UserDto(
        Long id,
        String email,
        String name,
        String password,
        Role role
) {
}
