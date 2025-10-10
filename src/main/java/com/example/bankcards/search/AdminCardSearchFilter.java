package com.example.bankcards.search;

import com.example.bankcards.entity.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AdminCardSearchFilter(
        LocalDate expiryDate,
        Long userId,
        CardStatus status,
        BigDecimal balance,
        Integer pageSize,
        Integer pageNumber
) {
}
