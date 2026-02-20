package com.linkedin.backend.features.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkedin.backend.features.authentication.model.AuthenticationUser;

public interface AuthenticationUserRepository extends JpaRepository<AuthenticationUser, Long> {

}
