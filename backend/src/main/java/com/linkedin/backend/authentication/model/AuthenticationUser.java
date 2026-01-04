package com.linkedin.backend.authentication.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity(name = "user")
public class AuthenticationUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String password;
	
	
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
	
	

}
