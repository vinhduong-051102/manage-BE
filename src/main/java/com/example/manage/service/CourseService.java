package com.example.manage.service;

import com.example.manage.model.Course;
import com.example.manage.request.CreateCourseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CourseService {
    Page<Course> getAllCourses(Pageable pageable);
    List<Course> getAllCourses();

    Course createCourse(CreateCourseRequest request);

    void updateCourse(Course course);

    void deleteCourse(Long id);

    Course getCourseById(Long id);

    List<Course> getListCourseNotTakenByUser(Long studentId);
    List<Course> getListCourseTakenByUserId(Long studentId);
    List<Course> getTopCoursesWithMostStudents(String limit);
    Page<Course> getCoursesByKeyword(String keyword, Pageable pageable);
}
