package com.linkedin.backend.features.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.linkedin.backend.features.authentication.service.AuthenticationService;

@RestController 
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
	
	public final AuthenticationService authenticationService;
	
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@GetMapping("/user")
	public AuthenticationUser getUser() {
		return authenticationService.getUser("puja@email.com");
	}

}
