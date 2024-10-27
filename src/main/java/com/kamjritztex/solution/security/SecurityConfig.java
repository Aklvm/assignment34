package com.kamjritztex.solution.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Configures the security filter chain for the application.
     *
     * This method sets up HTTP security configurations, including CSRF protection,
     * authorization rules for different user roles, and session management.
     * It specifies that:
     * <ul>
     * <li>Endpoints under "user/**" require the "USER" role.</li>
     * <li>Endpoints under "admin/**" require the "ADMIN" role.</li>
     * <li>Endpoints under "superAdmin/**" require the "SUPERADMIN" role.</li>
     * <li>Public access is granted to "email/login" and "createSuperAdmin".</li>
     * </ul>
     * It also configures the application to use stateless sessions and sets up a
     * JWT authentication filter.
     *
     * @param http the {@link HttpSecurity} object to be configured
     * @return a configured {@link SecurityFilterChain} for the application
     * @throws Exception if an error occurs during the configuration of the security
     *                   filter chain
     */

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.requestMatchers("user/**").hasRole("USER") // User-specific endpoints
                        .requestMatchers("admin/**").hasRole("ADMIN")
                        .requestMatchers("superAdmin/**").hasRole("SUPERADMIN")
                        .requestMatchers("email/login", "createSuperAdmin")
                        .permitAll() // Admin-specific endpoints
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

}
