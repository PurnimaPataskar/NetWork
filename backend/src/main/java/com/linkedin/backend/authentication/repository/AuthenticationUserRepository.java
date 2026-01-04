package com.linkedin.backend.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.linkedin.backend.authentication.model.AuthenticationUser;

public interface AuthenticationUserRepository extends JpaRepository<AuthenticationUser, Long> {

}
