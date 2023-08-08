package com.example.manage.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ActiveRequest {
    private Long id;
    private String password;
}
