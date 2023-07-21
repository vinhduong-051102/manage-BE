package com.example.manage.service.impl;

import com.example.manage.model.Period;
import com.example.manage.repository.PeriodRepository;
import com.example.manage.service.PeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodServiceImpl implements PeriodService {

    private final PeriodRepository periodRepository;
    @Override
    public Period findById(Long id) {
        return periodRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Period is not found by id"));
    }

    @Override
    public List<Period> findAll() {
        return periodRepository.findAll();
    }
}
