package com.example.bankcards.rs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRs {
    private String token;
    private String tokenType = "Bearer";
    private String email;
    private String role;
}
