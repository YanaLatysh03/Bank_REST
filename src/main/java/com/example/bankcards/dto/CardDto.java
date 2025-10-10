package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardDto(
        Long id,
        Long userId,
        String maskedNumber,
        LocalDate expiryDate,
        CardStatus status,
        BigDecimal balance
) {
}
