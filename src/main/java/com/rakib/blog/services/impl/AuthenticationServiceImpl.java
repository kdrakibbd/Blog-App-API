package com.rakib.blog.services.impl;

import com.rakib.blog.entities.Role;
import com.rakib.blog.entities.Token;
import com.rakib.blog.entities.User;
import com.rakib.blog.payloads.AuthenticationResponse;
import com.rakib.blog.payloads.LoginRequest;
import com.rakib.blog.payloads.RegisterRequest;
import com.rakib.blog.payloads.UserDto;
import com.rakib.blog.repository.TokenRepo;
import com.rakib.blog.repository.UserRepo;
import com.rakib.blog.security.JwtService;
import com.rakib.blog.security.UserDetailsServiceImp;
import com.rakib.blog.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private TokenRepo tokenRepo;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {

        if(this.userRepo.findByEmail(registerRequest.getEmail()).isPresent()) {
            return new AuthenticationResponse(null, null,"User already exist");
        }
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setAbout(registerRequest.getAbout());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        user.setRole(Role.USER);

        user = userRepo.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken,"User registration was successful");
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {

        String accessToken = null;
        String refreshToken = null;
        try {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(loginRequest.getEmail());
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, loginRequest.getPassword(), userDetails.getAuthorities()
            );
            Authentication authenticate = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            accessToken = this.jwtService.generateAccessToken((User) userDetails);
            refreshToken = this.jwtService.generateRefreshToken((User) userDetails);

            revokeAllTokenByUser((User) userDetails);
            saveUserToken(accessToken, refreshToken, (User) userDetails);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new AuthenticationResponse(accessToken, refreshToken, "User logged in successfully");
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokenListByUser = this.tokenRepo.findAllAccessTokensByUser(user.getId());
        if (!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(token -> {
                token.setLoggedOut(true);
            });
        }
        this.tokenRepo.saveAll(validTokenListByUser);
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setUser(user);
        token.setLoggedOut(false);
        this.tokenRepo.save(token);
    }

    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletRequest response) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);

        User user = this.userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("No user found"));

        if(this.jwtService.isValidRefreshToken(token, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity<>(new AuthenticationResponse(accessToken, refreshToken, "New token generated"), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
