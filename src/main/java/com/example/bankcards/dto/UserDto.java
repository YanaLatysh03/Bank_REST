package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;

public record UserDto(
        Long id,
        String username,
        String password,
        Role role
) {
}
