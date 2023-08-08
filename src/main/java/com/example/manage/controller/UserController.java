package com.example.manage.controller;

import com.example.manage.request.ActiveRequest;
import com.example.manage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/active")
    public ResponseEntity<String> activeAccount(@RequestBody ActiveRequest request) {
        userService.activeUser(request.getId(), request.getPassword());
        return ResponseEntity.ok("Active successfully");
    }

}
