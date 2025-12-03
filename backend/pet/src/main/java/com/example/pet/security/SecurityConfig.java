package com.example.pet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(auth -> auth

                // ---------------------------
                // PUBLIC ENDPOINTS
                // ---------------------------
                .requestMatchers("/api/auth/**").permitAll()        // login
                .requestMatchers("/api/users").permitAll()           // registration

                // public read-only pet browsing
                .requestMatchers("/api/pets", "/api/pets/search/**", "/api/pets/species/**", "/api/pets/status/**").permitAll()

                // public list shelters ONLY
                .requestMatchers("/api/shelters").permitAll()

                // ---------------------------
                // ADMIN ONLY
                // ---------------------------
                .requestMatchers("/api/shelters/**").hasRole("ADMIN")

                // ---------------------------
                // ORG USER ONLY
                // ---------------------------
                .requestMatchers("/api/pets/my-pets").hasRole("ORG_USER")
                .requestMatchers("/api/pets/**").hasRole("ORG_USER") // create/update/delete pets

                // ---------------------------
                // PUBLIC USER ONLY
                // ---------------------------
                .requestMatchers("/api/pets/my-adopted").hasRole("PUBLIC_USER")

                // ---------------------------
                // EVERYTHING ELSE REQUIRES AUTH
                // ---------------------------
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
