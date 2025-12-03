// SecurityConfig.java
package com.example.pet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

        http
            .csrf().disable()
            .cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(auth -> auth

                // Authentication & registration (public)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users").permitAll()

                // ---- Public read-only pet endpoints (allow GET only) ----
                // Allow public GET for listing, details, search, status, species
                .requestMatchers(HttpMethod.GET, "/api/pets", "/api/pets/**", "/api/pets/search/**", "/api/pets/species/**", "/api/pets/status/**")
                    .permitAll()

                // Allow public list shelters
                .requestMatchers(HttpMethod.GET, "/api/shelters", "/api/shelters/**")
                    .permitAll()

                // ---- Admin only endpoints ----
                .requestMatchers("/api/shelters/**").hasRole("ADMIN")

                // ---- ORG_USER endpoints: create/update/delete and org-specific ----
                // Protect any non-GET pet endpoints (creation / modification)
                .requestMatchers(HttpMethod.POST, "/api/pets/**").hasRole("ORG_USER")
                .requestMatchers(HttpMethod.PUT, "/api/pets/**").hasRole("ORG_USER")
                .requestMatchers(HttpMethod.DELETE, "/api/pets/**").hasRole("ORG_USER")

                // Org user's own endpoints
                .requestMatchers("/api/pets/my-pets").hasRole("ORG_USER")

                // Public user endpoints that require auth (example: my-adopted)
                .requestMatchers("/api/pets/my-adopted").hasRole("PUBLIC_USER")

                // All other requests require authentication
                .anyRequest().authenticated()
            );

        // JWT filter
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
