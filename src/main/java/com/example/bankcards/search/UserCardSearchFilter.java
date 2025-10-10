package com.example.bankcards.search;

import com.example.bankcards.entity.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserCardSearchFilter(
        LocalDate expiryDate,
        CardStatus status,
        BigDecimal balance,
        Integer pageSize,
        Integer pageNumber
) {
}
