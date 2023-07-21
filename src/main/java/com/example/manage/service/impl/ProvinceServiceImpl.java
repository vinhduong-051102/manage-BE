package com.example.manage.service.impl;

import com.example.manage.model.Province;
import com.example.manage.repository.ProvinceRepository;
import com.example.manage.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {
    private final ProvinceRepository provinceRepository;
    @Override
    public Province findProvinceByCode(String code) {
        return provinceRepository.findByCode(code).orElseThrow(() -> new UsernameNotFoundException("Province not found"));
    }
}
