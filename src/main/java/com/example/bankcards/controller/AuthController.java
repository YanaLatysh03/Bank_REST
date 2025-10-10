package com.example.bankcards.controller;

import com.example.bankcards.rq.AuthRq;
import com.example.bankcards.rq.RegisterRq;
import com.example.bankcards.rs.AuthRs;
import com.example.bankcards.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
@Data
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<AuthRs> register(
            @RequestBody RegisterRq request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    public ResponseEntity<AuthRs> login(
            @RequestBody AuthRq request
    ) {
        return ResponseEntity.ok(authService.login(request.email(), request.password()));
    }
}
