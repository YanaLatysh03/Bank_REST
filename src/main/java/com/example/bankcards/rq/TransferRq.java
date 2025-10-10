package com.example.bankcards.rq;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Модель запроса на перевод средств между картами")
public record TransferRq(

        @Schema(description = "ID карты-источника, с которой списываются средства")
        Long sourceCardId,

        @Schema(description = "ID карты-получателя, на которую поступают средства")
        Long destinationCardId,

        @Schema(description = "Сумма перевода. Должна быть положительной", example = "150.00", minimum = "0.01")
        BigDecimal amount
) {
}
