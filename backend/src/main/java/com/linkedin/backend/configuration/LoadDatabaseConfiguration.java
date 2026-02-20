package com.linkedin.backend.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.linkedin.backend.features.authentication.repository.AuthenticationUserRepository;
import com.linkedin.backend.features.authentication.utils.Encoder;

@Configuration
public class LoadDatabaseConfiguration {
	
	public final Encoder encoder;
	
	public LoadDatabaseConfiguration(Encoder encoder) {
		this.encoder = encoder;
	}
	
	@Bean
	public CommandLineRunner initDatabase (AuthenticationUserRepository authenticationUserRepository) {
		return args -> {
			AuthenticationUser authenticationUser = new AuthenticationUser("puja@email.com", encoder.encode("mypassword"));
			authenticationUserRepository.save(authenticationUser);
			AuthenticationUser authenticationUser2 = new AuthenticationUser("puja2@email.com", "mypassword2");
			authenticationUserRepository.save(authenticationUser2);
		};
	}

}
