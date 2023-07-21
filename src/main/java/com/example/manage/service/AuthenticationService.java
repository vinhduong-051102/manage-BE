package com.example.manage.service;


import com.example.manage.config.JwtService;
import com.example.manage.dto.DataMailDTO;
import com.example.manage.model.ETokenType;
import com.example.manage.model.Token;
import com.example.manage.model.User;
import com.example.manage.repository.TokenRepository;
import com.example.manage.repository.UserRepository;
import com.example.manage.request.AuthenticationRequest;
import com.example.manage.request.RegisterRequest;
import com.example.manage.response.AuthenticationResponse;
import com.example.manage.response.SigninResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final MailService mailService;

  public AuthenticationResponse register(RegisterRequest request) throws MessagingException {
    String originPassword = request.getPassword();
    var user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .isActive(request.getIsActive())
            .build();
    var savedUser = repository.save(user);
    Thread emailThread = new Thread(() -> {
      if (!user.getEmail().equals("admin@gmail.com")) {
        DataMailDTO dataMail = new DataMailDTO();
        dataMail.setTo(user.getEmail());
        dataMail.setSubject("XÁC NHẬN TẠO MỚI THÔNG TIN NGƯỜI DÙNG");

        Map<String, Object> props = new HashMap<>();
        props.put("password", originPassword);
        props.put("email", user.getEmail());
        dataMail.setProps(props);
        try {
          mailService.sendHtmlMail(dataMail, "client");
        } catch (MessagingException e) {
          throw new RuntimeException(e);
        }
      }
    });
    emailThread.start();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  public AuthenticationResponse register(User user) {
    String originPassword = user.getPassword();
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    var savedUser = repository.save(user);
    Thread emailThread = new Thread(() -> {
      if (!user.getEmail().equals("admin@gmail.com")) {
        DataMailDTO dataMail = new DataMailDTO();
        dataMail.setTo(user.getEmail());
        dataMail.setSubject("XÁC NHẬN TẠO MỚI THÔNG TIN NGƯỜI DÙNG");

        Map<String, Object> props = new HashMap<>();
        props.put("password", originPassword);
        props.put("email", user.getEmail());
        dataMail.setProps(props);
        try {
          mailService.sendHtmlMail(dataMail, "client");
        } catch (MessagingException e) {
          throw new RuntimeException(e);
        }
      }
    });
    emailThread.start();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  public SigninResponse authenticate(@NotNull AuthenticationRequest request) {
    User user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    String username = "admin";
    if(user.getStudent() != null) {
      username = user.getStudent().getName();
    }
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return SigninResponse.
            builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .email(user.getEmail())
            .userId(user.getId())
            .role(user.getRole())
            .fullName(username)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(ETokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(@NotNull User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          @NotNull HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
