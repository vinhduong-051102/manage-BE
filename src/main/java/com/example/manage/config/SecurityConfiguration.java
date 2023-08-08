package com.example.manage.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.manage.model.EPermission.*;
import static com.example.manage.model.ERole.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults())
        .csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers("/api/v1/auth/**", "/api/v1/location/**", "/api/v1/weekday/**", "/api/v1/period/**").permitAll()
            .requestMatchers("/api/v1/student/list").hasAnyRole(STUDENT.name(), ADMIN.name())
            .requestMatchers("/api/v1/student/cancel").hasAnyRole(STUDENT.name())
            .requestMatchers("/api/v1/course/getListByTime").hasAnyRole(STUDENT.name())
            .requestMatchers(POST, "/api/v1/student/enroll").hasAnyRole(STUDENT.name())
            .requestMatchers(GET, "/api/v1/course/containByUserId").hasAnyRole(STUDENT.name())
            .requestMatchers(PUT, "/api/v1/student/**").hasAnyRole(ADMIN.name())
            .requestMatchers(DELETE, "/api/v1/student/**").hasAnyRole(ADMIN.name())
            .requestMatchers("/api/v1/student/**").hasAnyRole(ADMIN.name())
            .requestMatchers("/api/v1/course/**").hasAnyRole(ADMIN.name())
            .requestMatchers("/api/v1/user/active").hasAnyRole(STUDENT.name())
        .anyRequest()
          .authenticated()
        .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
