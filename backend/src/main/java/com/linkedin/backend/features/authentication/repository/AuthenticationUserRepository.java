package com.linkedin.backend.features.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkedin.backend.features.authentication.model.AuthenticationUser;

public interface AuthenticationUserRepository extends JpaRepository<AuthenticationUser, Long> {
	
	Optional<AuthenticationUser> findByEmail(String email);
}
