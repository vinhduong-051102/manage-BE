package com.example.manage.response;

import com.example.manage.model.ERole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SigninResponse {
    private Long userId;
    private String fullName;
    private String email;
    @Enumerated(EnumType.STRING)
    private ERole role;
    private String accessToken;
    private String refreshToken;
    private Boolean isActive;
}
