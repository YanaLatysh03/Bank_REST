package com.example.bankcards.search;

public record AdminUserSearchFilter(
        String name,
        String email,
        Integer pageSize,
        Integer pageNumber
) {
}
