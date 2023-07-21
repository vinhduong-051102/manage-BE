package com.example.manage.service.impl;

import com.example.manage.exception.DescriptionRequiredException;
import com.example.manage.exception.DuplicateContentException;
import com.example.manage.exception.NameRequiredException;
import com.example.manage.exception.PageNumberToLargeException;
import com.example.manage.model.Course;
import com.example.manage.model.CourseTime;
import com.example.manage.model.Student;
import com.example.manage.repository.CourseRepository;
import com.example.manage.repository.CourseTimeRepository;
import com.example.manage.repository.StudentCourseRepository;
import com.example.manage.repository.StudentRepository;
import com.example.manage.request.CreateCourseRequest;
import com.example.manage.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final StudentCourseRepository studentCourseRepository;

    private final StudentRepository studentRepository;

    private final CourseTimeRepository courseTimeRepository;

    public Page<Course> getAllCourses(@NotNull Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course createCourse(@NotNull CreateCourseRequest request) {
        if(request.getName().isEmpty()) {
            throw new NameRequiredException("Course name must have value");
        }
        if(request.getDescription().isEmpty()) {
            throw new DescriptionRequiredException("Course description must have value");
        }
        Course course = courseRepository.save(Course
                .builder()
                .name(request.getName())
                .description(request.getDescription())
                .build());
        CourseTime courseTime = CourseTime
                .builder()
                .weekDay(request.getWeekDay())
                .periods(request.getPeriods())
                .course(course)
                .build();
        CourseTime c = courseTimeRepository.save(courseTime);
        return course;
    }

    public void updateCourse(@NotNull Course course) {
        if(course.getName().isEmpty()) {
            throw new NameRequiredException("Course name must have value");
        }
        if(course.getDescription().isEmpty()) {
            throw new DescriptionRequiredException("Course description must have value");
        }
        Course c = courseRepository.findById(course.getId()).orElse(null);
        if(c != null) {
            if(Objects.equals(c.getName(), course.getName()) && Objects.equals(c.getDescription(), course.getDescription())) {
                throw new DuplicateContentException("Content is not changed");
            }
        }
        else {
            throw new IllegalArgumentException("Course is not found");
        }
        courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not " +
                        "found with ID: " + id));

        List<Student> students = course.getStudents();
        for (Student s : students) {
            studentCourseRepository.deleteByStudentIdAndCourseId(s.getId(),
                    id);
        }
        courseRepository.delete(course);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not " +
                "found with ID: " + id));
    }

    @Override
    public List<Course> getListCourseNotTakenByUser(Long studentId) {
        studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not " +
                "found with ID"));
        return courseRepository.findAllByStudentsNotContaining(studentId).orElse(new ArrayList<>());
    }

    @Override
    public List<Course> getListCourseTakenByUserId(Long studentId) {
        studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not " +
                "found with ID"));
        return courseRepository.findCoursesTakenByUserId(studentId).orElse(new ArrayList<>());
    }

    @Override
    public List<Course> getTopCoursesWithMostStudents(String limit) {
        try {
            int limitNumber = Integer.parseInt(limit);
            List<Course> listCourseArrange =
                    courseRepository.findCourseByMostAmountStudent();
            if(limitNumber < listCourseArrange.size()) {
                return courseRepository.findCourseByMostAmountStudent().subList(0
                        , limitNumber);
            }
            return courseRepository.findCourseByMostAmountStudent();
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Amount: " + limit + "is not a number");
        }
    }

    @Override
    public Page<Course> getCoursesByKeyword(String keyword, Pageable pageable) {
        List<Course> courses = courseRepository.findAll();
        int totalStudent = courses.size();
        int pageSize = pageable.getPageSize();
        int maxPages = (totalStudent + pageSize - 1) / pageSize;
        if(maxPages < pageable.getPageNumber()) {
            throw new PageNumberToLargeException("Page number to large, please select between 1 to " + maxPages);
        }
        return courseRepository.findByKeyword(keyword, pageable);
    }

}
