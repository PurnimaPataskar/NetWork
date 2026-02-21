package com.linkedin.backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BackendController {
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public  ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		return ResponseEntity.badRequest().body(Map.of("message", "Required request body is missing"));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public  ResponseEntity<Map<String, String>> handleMethodArgumentValidException(MethodArgumentNotValidException e) {
		StringBuilder errorMessage = new StringBuilder();
		e.getBindingResult().getFieldErrors().forEach(error -> 
				errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; "));
		return ResponseEntity.badRequest().body(Map.of("message", errorMessage.toString()));
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
	}

}

