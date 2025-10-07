package com.example.bankcards.filter;

import java.time.LocalDate;

public record AdminCardSearchFilter(
        LocalDate expiryDate,
        Long userId,
        Integer pageSize,
        Integer pageNumber
) {
}
