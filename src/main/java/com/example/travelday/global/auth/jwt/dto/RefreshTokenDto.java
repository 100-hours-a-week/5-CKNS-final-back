package com.example.travelday.global.auth.jwt.dto;

import lombok.Builder;

@Builder
public record RefreshTokenDto(
        String token,
        long expiresIn
) {

    public int getExpiresInSecond() {
        return (int) ((expiresIn - System.currentTimeMillis()) / 1000);
    }
}
