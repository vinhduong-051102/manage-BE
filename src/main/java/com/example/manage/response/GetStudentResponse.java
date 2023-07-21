package com.example.manage.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStudentResponse {
    private Long id;
    private String name;
    private String address;
    private String provinceCode;
    private String districtCode;
    private String wardCode;
    private String email;
    private Boolean isActive;
    private String locationString;
}
