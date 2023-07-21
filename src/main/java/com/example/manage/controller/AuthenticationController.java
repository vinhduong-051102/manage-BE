package com.example.manage.controller;

import com.example.manage.config.LogoutService;
import com.example.manage.request.AuthenticationRequest;
import com.example.manage.request.RegisterRequest;
import com.example.manage.response.AuthenticationResponse;
import com.example.manage.response.SigninResponse;
import com.example.manage.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  private final LogoutService logoutService;

  @PostMapping("/signup")
  public ResponseEntity<AuthenticationResponse> signup(
      @RequestBody RegisterRequest request
  ) throws MessagingException {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/signin")
  public ResponseEntity<SigninResponse> signin(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }

  @PostMapping("logout")
  public void signout(HttpServletRequest request) {
    logoutService.logout(request, null, null);
  }
}
