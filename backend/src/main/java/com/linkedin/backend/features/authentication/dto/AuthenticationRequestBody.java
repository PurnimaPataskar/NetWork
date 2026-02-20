package com.linkedin.backend.features.authentication.dto;

public class AuthenticationRequestBody {
	
	public String email;
	public String password;
	
	public AuthenticationRequestBody(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setEmail() {
		this.email = email;
	}

}
