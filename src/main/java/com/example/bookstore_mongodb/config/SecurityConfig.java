package com.example.bookstore_mongodb.config;

import com.example.bookstore_mongodb.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/books/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/books/**").hasRole("USER")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")   // Only ADMIN users can delete books
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionConfigurer -> exceptionConfigurer
                        // Runs when the user is authenticated but does not have the required role for the endpoint
                        .accessDeniedHandler((httpRequest, httpResponse, accessDeniedError) -> {
                            // Set HTTP status code 403 Forbidden
                            httpResponse.setStatus(403);

                            // Tell the client we are returning JSON
                            httpResponse.setContentType("application/json");

                            // Write a JSON error message in the response body
                            httpResponse.getWriter().write("{\"message\": \"Access Denied\"}");
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}