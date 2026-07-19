package com.linkedin.backend.features.authentication.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.linkedin.backend.features.authentication.dto.AuthenticationRequestBody;
import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.linkedin.backend.features.authentication.repository.AuthenticationUserRepository;
import com.linkedin.backend.features.authentication.utils.EmailService;
import com.linkedin.backend.features.authentication.utils.Encoder;
import com.linkedin.backend.features.authentication.utils.JsonWebToken;

class AuthenticationServiceTest {

    @Test
    void registerSendsWelcomeEmail() throws Exception {
        Encoder encoder = mock(Encoder.class);
        AuthenticationUserRepository repository = mock(AuthenticationUserRepository.class);
        JsonWebToken jsonWebToken = mock(JsonWebToken.class);
        EmailService emailService = mock(EmailService.class);

        when(encoder.encode("secret123")).thenReturn("encoded-password");
        when(jsonWebToken.generateToken("user@example.com")).thenReturn("jwt-token");
        when(repository.save(any(AuthenticationUser.class))).thenReturn(null);

        AuthenticationService authenticationService = new AuthenticationService(encoder, repository, jsonWebToken, emailService);

        authenticationService.register(new AuthenticationRequestBody("user@example.com", "secret123"));

        verify(emailService).sendEmail(
            eq("user@example.com"),
            eq("Welcome to NetWork"),
            contains("Welcome")
        );
    }
}
