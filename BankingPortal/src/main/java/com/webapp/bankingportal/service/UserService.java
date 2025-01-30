package com.webapp.bankingportal.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.webapp.bankingportal.Entity.User;
import com.webapp.bankingportal.Exception.InvalidTokenException;
import com.webapp.bankingportal.dto.LoginRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
	public ResponseEntity<String> registerUser(User user);
	
	public ResponseEntity<String> login(LoginRequest loginRequest , HttpServletRequest request)
	throws InvalidTokenException;
	
	public ModelAndView logout(String token) throws InvalidTokenException;

}
