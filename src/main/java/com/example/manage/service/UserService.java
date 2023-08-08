package com.example.manage.service;

import com.example.manage.model.User;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createUser(User user) throws MessagingException;
    User findUserByEmail(String email);
    void activeUser(Long id, String newPassword);
    User findById(Long id);
}
