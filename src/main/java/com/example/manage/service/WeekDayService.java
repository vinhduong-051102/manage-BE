package com.example.manage.service;

import com.example.manage.model.WeekDay;

import java.util.List;

public interface WeekDayService {
    WeekDay findByNo(Long no);

    List<WeekDay> findAll();

}
