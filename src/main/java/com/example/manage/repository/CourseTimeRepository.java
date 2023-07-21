package com.example.manage.repository;

import com.example.manage.model.CourseTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseTimeRepository extends JpaRepository<CourseTime, Long> {
}
