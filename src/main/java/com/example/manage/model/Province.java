package com.example.manage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Province {
    @Id
    private Long id;
    @Column
    private String name;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "province_level")
    private String provinceLevel;
    @Column(name = "lang_id")
    private Long langId;
    @Column
    private String city;
    @Column
    private String code;
}
