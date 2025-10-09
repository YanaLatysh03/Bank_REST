package com.example.bankcards.rs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String email;
    private String role;
}
