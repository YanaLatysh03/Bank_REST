package com.example.bankcards.rq;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Модель запроса на перевод средств между картами")
public record TransferRq(

        @Schema(description = "ID карты-источника, с которой списываются средства")
        @NotNull
        Long sourceCardId,

        @Schema(description = "ID карты-получателя, на которую поступают средства")
        @NotNull
        Long destinationCardId,

        @Schema(description = "Сумма перевода. Должна быть положительной", example = "150.00", minimum = "0.01")
        @NotNull
        @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0.00")
        BigDecimal amount
) {
}
