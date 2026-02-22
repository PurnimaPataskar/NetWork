package com.linkedin.backend.features.authentication.utils;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JsonWebToken {

	@Value("${jwt.secret.key}")
	private String secret;
	
	       public SecretKey getKey() {
		       // Ensure the secret is at least 32 bytes (256 bits) for HS256
		       byte[] keyBytes = secret.getBytes();
		       if (keyBytes.length < 32) {
			   throw new IllegalArgumentException("JWT secret key must be at least 256 bits (32 bytes) for HS256. Current length: " + keyBytes.length);
		       }
		       return Keys.hmacShaKeyFor(keyBytes);
	       }
	
	public String generateToken(String email) {
		return Jwts.builder()
				.subject(email)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() * 1000 * 60 * 60 *10))
				.signWith(getKey())
				.compact();
	}
	
	public String getEmailFromToken(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

}
