package com.example.manage.service;

import com.example.manage.model.District;

import java.util.List;

public interface DistrictService {
    List<District> findByProvinceCode(String code);

    District findDistrictByCode(String code);
}
