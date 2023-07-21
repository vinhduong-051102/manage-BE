package com.example.manage.repository;

import com.example.manage.model.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeekDayRepository extends JpaRepository<WeekDay, Long> {
    Optional<WeekDay> findByNo(Long no);
}
