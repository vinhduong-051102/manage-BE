package com.example.manage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class District {
    @Id
    private Long id;
    @Column
    private String name;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "district_level")
    private String districtLevel;
    @Column
    private String code;
    @Column(name = "province_code")
    private String provinceCode;
}
