package com.example.manage.repository;

import com.example.manage.model.Course;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @NotNull Optional<Course> findById(@NotNull Long id);
    @Query("SELECT c FROM Course c " +
            "WHERE NOT EXISTS (SELECT 1 FROM c.students s WHERE s.id = :userId)")
    Optional<List<Course>> findAllByStudentsNotContaining(@Param("userId") Long userId);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :userId")
    Optional<List<Course>> findCoursesTakenByUserId(@Param("userId") Long userId);
    @Query("SELECT c FROM Course c ORDER BY SIZE(c.students) DESC")
    List<Course> findCourseByMostAmountStudent();
    Optional<Course> findByName(String name);
    @Query(value = "SELECT * FROM Course WHERE name LIKE %:keyword%",
            nativeQuery = true)
    Page<Course> findByKeyword(@Param("keyword") String keyword,
                               Pageable pageable);

}
