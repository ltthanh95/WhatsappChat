package com.tl.whatapps.Config;

import java.util.Arrays;
import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class AppConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/public/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll() // Allow public access for debugging
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration cfg = new CorsConfiguration();

                    // ✅ Allow only your frontend, do not use "*"
                    cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
                    cfg.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000"));
                    cfg.setAllowCredentials(true);
                    cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    cfg.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                    cfg.setExposedHeaders(Arrays.asList("Authorization"));
                    cfg.setAllowCredentials(true); // Allow credentials, but origins must be explicit
                    cfg.setMaxAge(3600L);

                    return cfg;
                }))
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}
