package com.example.manage.repository;

import com.example.manage.model.CourseTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface CourseTimeRepository extends JpaRepository<CourseTime, Long> {
    @Query("SELECT ct FROM CourseTime ct " +
            "WHERE ct.weekDay = :weekDay ")
    List<CourseTime> findAllByWeekDay(Long weekDay);
}
