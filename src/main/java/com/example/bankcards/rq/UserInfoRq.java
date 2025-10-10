package com.example.bankcards.rq;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель запроса с информацией о пользователе")
public record UserInfoRq(

        @Schema(description = "Электронная почта пользователя", example = "yana@example.com")
        String email,

        @Schema(description = "Отображаемое имя пользователя", example = "Яна Латыш")
        String name,

        @Schema(description = "Пароль пользователя в незашифрованном виде", example = "StrongP@ssw0rd")
        String password
) {
}
