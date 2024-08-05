package com.tireshop.tiresShop.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        @Autowired
        private JWTAuthenticationFilter jwtAuthenticationFilter;

        @Autowired
        public SecurityConfig(CustomUserDetailsService userDetailsService,
                        JwtAuthEntryPoint authEntryPoint) {
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("users/**",
                                                                "tires/manufacturers/**", "/sizes/**",
                                                                "searchBy/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .sessionManagement((session) -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout -> logout
                                                .logoutUrl("/logout") // Define a logout URL (if needed)
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        // Custom logic after logout, e.g., logging, clearing cookies,
                                                        // etc.
                                                        response.setStatus(HttpServletResponse.SC_OK);
                                                })
                                                .invalidateHttpSession(true) // Invalidate session if using stateful
                                                                             // session management
                                                .deleteCookies("JSESSIONID") // Delete session cookie (if applicable)
                                );
                ;

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
