package com.linkedin.backend.features.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkedin.backend.features.authentication.dto.AuthenticationRequestBody;
import com.linkedin.backend.features.authentication.dto.AuthenticationResponseBody;
import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.linkedin.backend.features.authentication.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
	
	public final AuthenticationService authenticationService;
	
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@GetMapping("/user")
	public AuthenticationUser getUser(@RequestAttribute("authenticatedUser") AuthenticationUser user) {
		return user;
	}
	
	@PostMapping("/login")
	public AuthenticationResponseBody loginPage (@Valid @RequestBody AuthenticationRequestBody loginRequestBody) {
		return authenticationService.login(loginRequestBody);
	}
	
	
	@PostMapping("/register")
	public AuthenticationResponseBody registerPage (@Valid @RequestBody AuthenticationRequestBody registerRequestBody) {
		return authenticationService.register(registerRequestBody);
	}

	@PutMapping("/validate-email-verification-token")
	public String verifyEmail(@RequestParam String token, @RequestAttribute("authenticatedUser") AuthenticationUser user) {
		authenticationService.validateEmailVerificationToken(user.getEmail(), token);
		return "Email verified successfully.";
	}

	@PutMapping("/send-email-verification-token")
	public String sendEmailVerificationToken(@RequestAttribute("authenticatedUser") AuthenticationUser user) {
		authenticationService.sendEmailVerificationToken(user.getEmail());
		return "Email verification token sent successfully.";
	}

	@PutMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String newPassword, @RequestParam String email) {
		authenticationService.resetPassword(email, newPassword, token);
		return "Password reset successfully.";
	}

	@PutMapping("/send-password-reset-token")
	public String sendResetPasswordToken(@RequestParam String email) {
		authenticationService.sendPasswordResetToken(email);
		return "Password reset token sent successfully.";
	}
}
