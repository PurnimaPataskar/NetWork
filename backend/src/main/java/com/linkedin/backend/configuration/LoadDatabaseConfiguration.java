package com.linkedin.backend.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linkedin.backend.authentication.model.AuthenticationUser;
import com.linkedin.backend.authentication.repository.AuthenticationUserRepository;

@Configuration
public class LoadDatabaseConfiguration {
	
	@Bean
	public CommandLineRunner initDatabase (AuthenticationUserRepository authenticationUserRepository) {
		return args -> {
			AuthenticationUser authenticationUser = new AuthenticationUser("puja@email.com", "mypassword");
			authenticationUserRepository.save(authenticationUser);
		};
	}

}
