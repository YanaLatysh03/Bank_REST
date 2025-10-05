package com.example.bankcards.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardDto(
        Long id,
        String maskedNumber,
        LocalDate expiryDate,
        String status,
        BigDecimal balance
) {
}
