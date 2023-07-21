package com.example.manage.service.impl;

import com.example.manage.model.WeekDay;
import com.example.manage.repository.WeekDayRepository;
import com.example.manage.service.WeekDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekDayServiceImpl implements WeekDayService {

    private final WeekDayRepository weekDayRepository;
    @Override
    public WeekDay findByNo(Long no) {
        return weekDayRepository.findByNo(no).orElseThrow(() -> new UsernameNotFoundException("Week day is not found by number order"));
    }

    @Override
    public List<WeekDay> findAll() {
        return weekDayRepository.findAll();
    }
}
