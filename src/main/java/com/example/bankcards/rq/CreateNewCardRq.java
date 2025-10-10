package com.example.bankcards.rq;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Модель запроса на создание новой банковской карты")
public record CreateNewCardRq(

        @Schema(description = "Идентификатор пользователя, для которого создаётся карта")
        @NotNull
        Long userId
) {
}
