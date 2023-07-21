package com.example.manage.service.impl;

import com.example.manage.model.District;
import com.example.manage.repository.DistrictRepository;
import com.example.manage.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;
    @Override
    public List<District> findByProvinceCode(String code) {
        return districtRepository.findAllByProvinceCode(code);
    }

    @Override
    public District findDistrictByCode(String code) {
        return districtRepository.findByCode(code).orElseThrow(() -> new UsernameNotFoundException("District is not found"));
    }
}
