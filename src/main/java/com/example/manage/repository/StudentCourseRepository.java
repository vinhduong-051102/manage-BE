package com.example.manage.repository;

import com.example.manage.model.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    @Modifying
    @Query(value = "DELETE FROM student_course WHERE student_id = :studentId AND course_id = :courseId", nativeQuery = true)
    void deleteByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    @Query(value = "SELECT * FROM student_course WHERE student_id = " +
            ":studentId AND course_id = :courseId", nativeQuery = true)
    Optional<StudentCourse> findEnrollCourse(@Param("studentId") Long studentId,
                                      @Param("courseId") Long courseId);
}
