package com.example.manage.service.impl;

import com.example.manage.model.Ward;
import com.example.manage.repository.WardRepository;
import com.example.manage.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WardServiceImpl implements WardService {
    private final WardRepository wardRepository;
    @Override
    public List<Ward> findByDistrictCode(String code) {
        return wardRepository.findAllByDistrictCode(code);
    }

    @Override
    public Ward findWardByCode(String code) {
        return wardRepository.findByCode(code).orElseThrow(() -> new UsernameNotFoundException("Ward is not found"));
    }
}
