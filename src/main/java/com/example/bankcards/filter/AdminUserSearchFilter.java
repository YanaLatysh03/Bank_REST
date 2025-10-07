package com.example.bankcards.filter;

public record AdminUserSearchFilter(
        String name,
        String email,
        Integer pageSize,
        Integer pageNumber
) {
}
