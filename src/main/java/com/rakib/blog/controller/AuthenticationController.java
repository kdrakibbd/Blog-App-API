package com.rakib.blog.controller;

import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.AuthenticationResponse;
import com.rakib.blog.payloads.LoginRequest;
import com.rakib.blog.payloads.RegisterRequest;
import com.rakib.blog.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "01. Authentication Controller", description = "APIs for user registration, login, and token refresh")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @Operation(summary = "Register a new user", description = "Endpoint to register a new user with the provided details")
    @PostMapping("/register")
    ResponseEntity<ApiResponse<AuthenticationResponse>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthenticationResponse authResponse = this.authService.register(registerRequest);
        return ResponseEntity.ok(ApiResponse.success(authResponse.getMessage(), authResponse));
    }

    @Operation(summary = "User login", description = "Endpoint for user login with email and password")
    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthenticationResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthenticationResponse authResponse = this.authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(authResponse.getMessage(), authResponse));
    }

    @Operation(summary = "Refresh authentication token", description = "Endpoint to refresh the authentication token using the refresh token")
    @PostMapping("/refresh-token")
    ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<?> result = this.authService.refreshToken(request);
        if (result.getStatusCode().is2xxSuccessful() && result.getBody() instanceof AuthenticationResponse authResponse) {
            return ResponseEntity.ok(ApiResponse.success(authResponse.getMessage(), authResponse));
        }
        return ResponseEntity.status(result.getStatusCode()).build();
    }
}
