package com.example.manage.service;

import com.example.manage.model.Course;
import com.example.manage.request.CreateCourseRequest;
import com.example.manage.request.EditCourseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;


public interface CourseService {
    Page<Course> getAllCourses(Pageable pageable);
    List<Course> getAllCourses();

    Course createCourse(CreateCourseRequest request);

    void updateCourse(EditCourseRequest request);

    void deleteCourse(Long id);

    Course getCourseById(Long id);

    List<Course> getListCourseNotTakenByUser(Long studentId);
    Page<Course> getListCourseNotTakenByUser(Long studentId, Pageable pageable);
    List<Course> getListCourseTakenByUserId(Long studentId);
    Page<Course> getListCourseTakenByUserId(Long studentId, Pageable pageable);
    List<Course> getTopCoursesWithMostStudents(String limit);
    Page<Course> getCoursesByKeyword(String keyword, Pageable pageable);
    List<Course> getCoursesByKeyword(String keyword);
    List<Course> getListCourseByTime(Long weekDay, Set<Long> periods, Long userId);
    List<Course> filterCourse(Long status, Long enrollStatus, Long weekday);
}
