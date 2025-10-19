package com.rakib.blog.config;

import com.rakib.blog.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String[] PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/public/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    };

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomLogoutHandler logoutHandler;

    @Autowired
    CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req->req.requestMatchers(PUBLIC_URLS).permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/users").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/user/{userId}/save").hasAuthority("USER")
                                .requestMatchers(HttpMethod.GET).permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/{userId}", "/api/v1/categories/{categoryId}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE).hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/categories").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST).hasAuthority("USER")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/categories/{categoryId}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT).hasAuthority("USER")
                                .anyRequest()
                                .authenticated()
                ).userDetailsService(userDetailsServiceImp)
                .exceptionHandling(e->e.accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(l->l
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()
                        ))
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}