package com.example.bankcards.rq;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Модель запроса с информацией о пользователе")
public record UserInfoRq(

        @Schema(description = "Электронная почта пользователя", example = "yana@example.com")
        @Email
        String email,

        @Schema(description = "Отображаемое имя пользователя", example = "Яна Латыш")
        @NotNull
        String name,

        @Schema(description = "Пароль пользователя в незашифрованном виде", example = "StrongP@ssw0rd")
        @NotNull
        String password
) {
}
