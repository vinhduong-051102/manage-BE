package com.example.manage.controller;

import com.example.manage.model.WeekDay;
import com.example.manage.service.WeekDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/weekday")
public class WeekDayController {
    private final WeekDayService weekDayService;

    @GetMapping("/all")
    public ResponseEntity<List<WeekDay>> getAll() {
        return ResponseEntity.ok(weekDayService.findAll());
    }


}
