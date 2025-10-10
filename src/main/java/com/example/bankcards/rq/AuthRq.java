package com.example.bankcards.rq;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель запроса на авторизацию пользователя")
public record AuthRq(

        @Schema(description = "Электронная почта пользователя. Используется как логин для входа", example = "user@example.com")
        String email,

        @Schema(description = "Пароль пользователя в незашифрованном виде", example = "P@ssw0rd123")
        String password
) {
}
