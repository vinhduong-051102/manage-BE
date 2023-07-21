package com.example.manage.controller;

import com.example.manage.model.Period;
import com.example.manage.service.PeriodService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/period")
@RestController
public class PeriodController {
    private final PeriodService periodService;

    @GetMapping("/all")
    public ResponseEntity<List<Period>> getAll() {
        return ResponseEntity.ok(periodService.findAll());
    }

}
