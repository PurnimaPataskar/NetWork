package com.linkedin.backend.features.authentication.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class AuthenticationUserTest {

    @Test
    void shouldStoreResetPasswordTokenAndExpiry() {
        AuthenticationUser user = new AuthenticationUser("test@example.com", "password");
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        user.setResetPasswordToken("reset-token");
        user.setResetPasswordTokenExpiryDate(expiry);

        assertEquals("reset-token", user.getResetPasswordToken());
        assertEquals(expiry, user.getResetPasswordTokenExpiryDate());
    }
}
