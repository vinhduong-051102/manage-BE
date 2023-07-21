package com.example.manage.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentRequest {
    private String email;
    private String username;
    private String address;
    private String provinceCode;
    private String districtCode;
    private String wardCode;
}
