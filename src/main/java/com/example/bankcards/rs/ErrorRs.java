package com.example.bankcards.rs;

import java.time.LocalDateTime;

public record ErrorRs(
        String message,
        String detailMessage,
        LocalDateTime errorTime
) {
}
