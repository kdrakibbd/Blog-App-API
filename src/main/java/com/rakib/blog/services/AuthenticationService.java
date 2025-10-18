package com.rakib.blog.services;

import com.rakib.blog.payloads.LoginRequest;
import com.rakib.blog.payloads.AuthenticationResponse;
import com.rakib.blog.payloads.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(LoginRequest request);
    ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletRequest response);
}
