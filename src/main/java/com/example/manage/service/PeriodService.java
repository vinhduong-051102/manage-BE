package com.example.manage.service;

import com.example.manage.model.Period;

import java.util.List;

public interface PeriodService {
    Period findById(Long id);

    List<Period> findAll();

}
