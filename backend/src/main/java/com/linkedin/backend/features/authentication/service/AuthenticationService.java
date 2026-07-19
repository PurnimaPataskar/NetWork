package com.linkedin.backend.features.authentication.service;

import org.springframework.stereotype.Service;

import com.linkedin.backend.features.authentication.dto.AuthenticationRequestBody;
import com.linkedin.backend.features.authentication.dto.AuthenticationResponseBody;
import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.linkedin.backend.features.authentication.repository.AuthenticationUserRepository;
import com.linkedin.backend.features.authentication.utils.EmailService;
import com.linkedin.backend.features.authentication.utils.Encoder;
import com.linkedin.backend.features.authentication.utils.JsonWebToken;

import jakarta.validation.Valid;

@Service
public class AuthenticationService {
	private final Encoder encoder;
	private final JsonWebToken jsonWebToken;
	private final AuthenticationUserRepository authenticationUserRepository;
	private final EmailService emailService;
	
	public AuthenticationService(Encoder encoder, AuthenticationUserRepository authenticationUserRepository, JsonWebToken jsonWebToken, EmailService emailService) {
		this.encoder = encoder;
		this.jsonWebToken = jsonWebToken;
		this.authenticationUserRepository = authenticationUserRepository;
		this.emailService = emailService;
	}
	
	public AuthenticationUser getUser(String email) {
		return authenticationUserRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
	}

	public AuthenticationResponseBody register(AuthenticationRequestBody registerRequestBody) {
		authenticationUserRepository.save(new AuthenticationUser(registerRequestBody.getEmail(), encoder.encode(registerRequestBody.getPassword())));
		String token = jsonWebToken.generateToken(registerRequestBody.getEmail());
		try {
			emailService.sendEmail(registerRequestBody.getEmail(), "Welcome to NetWork", "Welcome to NetWork! Your account has been created successfully.");
		} catch (Exception exception) {
			throw new RuntimeException("Failed to send welcome email", exception);
		}
		return new AuthenticationResponseBody(token, "user registerd successfully");
	}

	public AuthenticationResponseBody login(AuthenticationRequestBody loginRequestBody) {
		AuthenticationUser user = authenticationUserRepository.findByEmail(loginRequestBody.getEmail()).orElseThrow(() -> new IllegalArgumentException("User not found."));
		if (!encoder.matches(loginRequestBody.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Password is incorrect.");
		}
		
		String token = jsonWebToken.generateToken(loginRequestBody.getEmail());
		return new AuthenticationResponseBody(token, "Authentication succeeded.");
	}
}
