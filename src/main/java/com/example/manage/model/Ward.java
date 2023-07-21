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
public class Ward {
    @Id
    private Long id;
    @Column
    private String name;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "ward_level")
    private String wardLevel;
    @Column
    private String code;
    @Column(name = "province_code")
    private String provinceCode;
    @Column(name = "district_code")
    private String districtCode;
}
