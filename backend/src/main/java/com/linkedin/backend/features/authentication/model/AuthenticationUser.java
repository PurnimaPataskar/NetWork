package com.linkedin.backend.features.authentication.model;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


@Entity(name = "user")
public class AuthenticationUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@Email
	@Column(unique = true)
	private String email;
	private Boolean isEmailVerified = false;
	private String emailVerificationToken = null;
	private LocalDateTime emailVerificationTokenExpiryDate = null;
	@JsonIgnore
	private String password;
	private String resetPasswordToken = null;
	private LocalDateTime resetPasswordTokenExpiryDate = null;
	
	
	public AuthenticationUser(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	
	public AuthenticationUser() {
//		to conver to json to jave we need jaxon hece, we will be udsng jaxon library whivh need empty constructory
	}



	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}


	public boolean getEmailVerified() {
		return Boolean.TRUE.equals(isEmailVerified);
	}

	public String getEmailVerificationToken() {
		return emailVerificationToken;
	}

	public void setEmailVerificationToken(String emailVerificationToken) {
		this.emailVerificationToken = emailVerificationToken;
	}

	public LocalDateTime getEmailVerificationTokenExpiryDate() {
		return emailVerificationTokenExpiryDate;
	}

	public void setEmailVerificationTokenExpiryDate(LocalDateTime emailVerificationTokenExpiryDate) {
		this.emailVerificationTokenExpiryDate = emailVerificationTokenExpiryDate;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.isEmailVerified = emailVerified;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public LocalDateTime getResetPasswordTokenExpiryDate() {
		return resetPasswordTokenExpiryDate;
	}

	public void setResetPasswordTokenExpiryDate(LocalDateTime resetPasswordTokenExpiryDate) {
		this.resetPasswordTokenExpiryDate = resetPasswordTokenExpiryDate;
	}

}
