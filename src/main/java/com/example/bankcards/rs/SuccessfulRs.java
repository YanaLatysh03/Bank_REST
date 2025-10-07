package com.example.bankcards.rs;

import lombok.Data;

@Data
public class SuccessfulRs {
    private String message;
    private boolean success = true;

    public SuccessfulRs(String message) {
        this.message = message;
    }
}

