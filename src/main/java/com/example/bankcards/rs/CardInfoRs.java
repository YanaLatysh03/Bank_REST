package com.example.bankcards.rs;

import com.example.bankcards.entity.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardInfoRs(
        Long id,
        Long user_id,
        String maskedNumber,
        LocalDate expiryDate,
        CardStatus status,
        BigDecimal balance
) {
}
