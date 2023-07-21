package com.example.manage.repository;

import com.example.manage.model.Course;
import com.example.manage.model.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @NotNull Optional<Student> findById(@NotNull Long id);

    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId")
    Optional<List<Student>> findStudentsEnrollCourseId(@Param("courseId") Long courseId);
    @Query(value = "SELECT * FROM Student WHERE name LIKE %:keyword%",
            nativeQuery = true)
    Page<Student> findByKeyword(@Param("keyword") String keyword,
                               Pageable pageable);

    @Query(value = "SELECT * FROM Student WHERE name LIKE %:keyword%",
            nativeQuery = true)
    List<Student> findByKeyword(@Param("keyword") String keyword);

}
