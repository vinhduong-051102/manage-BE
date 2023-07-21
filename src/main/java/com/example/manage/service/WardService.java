package com.example.manage.service;

import com.example.manage.model.Ward;
import java.util.List;

public interface WardService {
    List<Ward> findByDistrictCode(String code);

    Ward findWardByCode(String code);

}
