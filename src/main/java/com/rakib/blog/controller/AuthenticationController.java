package com.rakib.blog.controller;

import com.rakib.blog.payloads.AuthenticationResponse;
import com.rakib.blog.payloads.LoginRequest;
import com.rakib.blog.payloads.RegisterRequest;
import com.rakib.blog.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(this.authService.register(registerRequest));
    }

    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(this.authService.login(loginRequest));
    }

    @PostMapping("/refresh-token")
    ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletRequest response) {
        return this.authService.refreshToken(request, response);
    }
}
