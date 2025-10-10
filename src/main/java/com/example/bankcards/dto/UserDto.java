package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;

public record UserDto(
        Long id,
        String email,
        String name,
        String password,
        Role role
) {
    public UserDto(Long id, String email, String name, String password, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public UserDto(String email) {
        this(null, email, null, null, null);
    }
}
