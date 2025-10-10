package com.example.bankcards.rq;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Модель запроса на регистрацию нового пользователя")
public record RegisterRq(

        @Schema(description = "Электронная почта пользователя. Используется как логин", example = "newuser@example.com")
        @NotNull
        @Email
        String email,

        @Schema(description = "Пароль пользователя в незашифрованном виде", example = "SecureP@ssw0rd")
        @NotNull
        String password,

        @Schema(description = "Отображаемое имя пользователя", example = "Яна Латыш")
        @NotNull
        String name
) {
}
