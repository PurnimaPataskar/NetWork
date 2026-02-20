package com.linkedin.backend.features.authentication.service;

import org.springframework.stereotype.Service;

import com.linkedin.backend.features.authentication.dto.AuthenticationRequestBody;
import com.linkedin.backend.features.authentication.dto.AuthenticationResponseBody;
import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.linkedin.backend.features.authentication.repository.AuthenticationUserRepository;

@Service
public class AuthenticationService {
	private final AuthenticationUserRepository authenticationUserRepository;
	
	public AuthenticationService(AuthenticationUserRepository authenticationUserRepository) {
		this.authenticationUserRepository = authenticationUserRepository;
	}
	
	public AuthenticationUser getUser(String email) {
		return authenticationUserRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
	}

	public AuthenticationResponseBody register(AuthenticationRequestBody registerRequestBody) {
		authenticationUserRepository.save(new AuthenticationUser(registerRequestBody.getEmail(), registerRequestBody.getPassword()));
		
		return new AuthenticationResponseBody("token", "user registerd successfully");
	}
}
