package com.example.pet.security;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - no authentication required
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pets").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pets/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pets/species/{species}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pets/search/name/{name}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/shelters").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/shelters/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/shelters/{id}/pets").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/shelters/{id}/users").permitAll()
                
                // Adoption requests - authenticated users only
                .requestMatchers(HttpMethod.POST, "/api/adoption-requests").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/adoption-requests/my-requests").authenticated()
                .requestMatchers("/api/adoption-requests/me/{id}").authenticated()
                
                // ADD THIS LINE: Allow authenticated access to add users to shelters
            .requestMatchers(HttpMethod.POST, "/api/shelters/{shelterId}/users").authenticated()

                // Admin only endpoints
                .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/email/{email}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/shelters").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/shelters/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/shelters/{shelterId}/users").hasRole("ADMIN")
                
                // Change from hasRole('ADMIN') to authenticated()
            .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").authenticated()
            
                // ORG_USER endpoints (pet management)
                .requestMatchers(HttpMethod.POST, "/api/pets").hasRole("ORG_USER")
                .requestMatchers(HttpMethod.PUT, "/api/pets/{id}").hasRole("ORG_USER")
                .requestMatchers(HttpMethod.DELETE, "/api/pets/{id}").hasRole("ORG_USER")
                
                // Adoption request management (ORG_USER and ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/adoption-requests").hasAnyRole("ORG_USER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/adoption-requests/{id}").hasAnyRole("ORG_USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/adoption-requests/{id}").hasAnyRole("ORG_USER", "ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}