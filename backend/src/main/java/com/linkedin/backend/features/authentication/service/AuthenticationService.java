package com.linkedin.backend.features.authentication.service;

import java.security.SecureRandom;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	private final int durationInMinutes = 2;

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
	
	public static String generateEmailVerificationToken() {
		SecureRandom random = new SecureRandom();
		StringBuilder token = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			int randomDigit = random.nextInt(10); // Generate a random digit between 0 and 9
			token.append(randomDigit);
		}
		return token.toString();
	}

	public void sendEmailVerificationToken(String email) {
		Optional<AuthenticationUser> user = authenticationUserRepository.findByEmail(email);
		if (user.isPresent() && !user.get().getEmailVerified()) {
		
			String emailVerificationToken = generateEmailVerificationToken();
			String hashedToken = encoder.encode(emailVerificationToken);

			user.get().setEmailVerificationToken(hashedToken);
			user.get().setEmailVerificationTokenExpiryDate(java.time.LocalDateTime.now().plusMinutes(durationInMinutes));
			authenticationUserRepository.save(user.get());
			String subject = "Email Verification";
			String body = String.format("Only one step to take full advantage of NetWork.\n\n"
					+ "Please use the following verification code to verify your email address:\n\n"
					+ "%s\n\n"
					+ "This code will expire in %d minutes.\n\n"
					+ "If you did not request this verification, please ignore this email.", emailVerificationToken, durationInMinutes);
			
			try {
				emailService.sendEmail(email, subject, body);
			} catch (Exception exception) {
				logger.info("Error sending email verification token to {}: {}", email, exception.getMessage());
			}
		} else {
			throw new IllegalArgumentException("User not found or email already verified.");
		}
	}

	public void validateEmailVerificationToken(String email, String token) {
		Optional<AuthenticationUser> user = authenticationUserRepository.findByEmail(email);
		if (token != null) token = token.trim();
		if (user.isPresent() && encoder.matches(token, user.get().getEmailVerificationToken()) && !user.get().getEmailVerificationTokenExpiryDate().isBefore(java.time.LocalDateTime.now())) {
			user.get().setEmailVerified(true);
			user.get().setEmailVerificationToken(null);
			user.get().setEmailVerificationTokenExpiryDate(null);
			authenticationUserRepository.save(user.get());
		} else if (user.isPresent() && encoder.matches(token,user.get().getEmailVerificationToken()) && user.get().getEmailVerificationTokenExpiryDate().isBefore(java.time.LocalDateTime.now())) {
			throw new IllegalArgumentException("Email verification token has expired.");
		} else if (user.isPresent() && !encoder.matches(token, user.get().getEmailVerificationToken())) {
			throw new IllegalArgumentException("Invalid email verification token.");
		} else {
			throw new IllegalArgumentException("User not found.");
		}
	}

	public void sendPasswordResetToken(String email) {
		AuthenticationUser user = authenticationUserRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("User not found."));

		String resetPasswordToken = generateEmailVerificationToken();
		String hashedResetToken = encoder.encode(resetPasswordToken);
		user.setResetPasswordToken(hashedResetToken);
		user.setResetPasswordTokenExpiryDate(java.time.LocalDateTime.now().plusMinutes(durationInMinutes));
		authenticationUserRepository.save(user);

		String subject = "Reset your password";
		String body = String.format("Use the following code to reset your password:\n\n%s\n\nThis code will expire in %d minutes.", resetPasswordToken, durationInMinutes);
		try {
			emailService.sendEmail(email, subject, body);
		} catch (Exception exception) {
			logger.info("Error sending password reset token to {}: {}", email, exception.getMessage());
		}
	}

	public void validatePasswordResetToken(String email, String token) {
		AuthenticationUser user = authenticationUserRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("User not found."));

		if (user.getResetPasswordToken() == null || user.getResetPasswordTokenExpiryDate() == null) {
			throw new IllegalArgumentException("No password reset token found.");
		}

		if (user.getResetPasswordTokenExpiryDate().isBefore(java.time.LocalDateTime.now())) {
			throw new IllegalArgumentException("Password reset token has expired.");
		}

		if (!encoder.matches(token, user.getResetPasswordToken())) {
			throw new IllegalArgumentException("Invalid password reset token.");
		}
	}

	public void resetPassword(String email, String token, String newPassword) {
		AuthenticationUser user = authenticationUserRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("User not found."));

		validatePasswordResetToken(email, token);
		user.setPassword(encoder.encode(newPassword));
		user.setResetPasswordToken(null);
		user.setResetPasswordTokenExpiryDate(null);
		authenticationUserRepository.save(user);
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
