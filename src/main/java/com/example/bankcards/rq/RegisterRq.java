package com.example.bankcards.rq;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель запроса на регистрацию нового пользователя")
public record RegisterRq(

        @Schema(description = "Электронная почта пользователя. Используется как логин", example = "newuser@example.com")
        String email,

        @Schema(description = "Пароль пользователя в незашифрованном виде", example = "SecureP@ssw0rd")
        String password,

        @Schema(description = "Отображаемое имя пользователя", example = "Яна Латыш")
        String name
) {
}
