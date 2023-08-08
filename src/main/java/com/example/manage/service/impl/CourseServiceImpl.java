package com.example.manage.service.impl;

import com.example.manage.exception.PageNumberToLargeException;
import com.example.manage.model.Course;
import com.example.manage.model.CourseTime;
import com.example.manage.model.Student;
import com.example.manage.model.User;
import com.example.manage.repository.*;
import com.example.manage.request.CreateCourseRequest;
import com.example.manage.request.EditCourseRequest;
import com.example.manage.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final StudentCourseRepository studentCourseRepository;

    private final StudentRepository studentRepository;

    private final CourseTimeRepository courseTimeRepository;

    private final UserRepository userRepository;

    public Page<Course> getAllCourses(@NotNull Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course createCourse(@NotNull CreateCourseRequest request) {
        Course course = courseRepository.save(Course
                .builder()
                .name(request.getName())
                .shortDescription(request.getShortDescription())
                .detailDescription(request.getDetailDescription())
                .beginDate(request.getBeginDate())
                .endDate(request.getEndDate())
                .build());
        CourseTime courseTime = CourseTime
                .builder()
                .weekDay(request.getWeekDay())
                .periods(request.getPeriods())
                .course(course)
                .build();
        courseTimeRepository.save(courseTime);
        return course;
    }

    public void updateCourse(@NotNull EditCourseRequest request) {
        Course course = courseRepository.findById(request.getId()).orElseThrow(() -> new UsernameNotFoundException("Course is not found with id"));
        course.getCourseTime().setWeekDay(request.getWeekDay());
        course.getCourseTime().setPeriods(request.getPeriods());
        course.setName(request.getName());
        course.setDetailDescription(request.getDetailDescription());
        course.setShortDescription(request.getShortDescription());
        course.setBeginDate(request.getBeginDate());
        course.setEndDate(request.getEndDate());
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
        return courseRepository.findAllByStudentsNotContaining(studentId);
    }

    @Override
    public Page<Course> getListCourseNotTakenByUser(Long studentId, Pageable pageable) {
        studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not " +
                "found with ID"));
        return courseRepository.findAllByStudentsNotContaining(studentId, pageable);
    }

    @Override
    public List<Course> getListCourseTakenByUserId(Long studentId) {
        studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not " +
                "found with ID"));
        return courseRepository.findCoursesTakenByUserId(studentId);
    }

    @Override
    public Page<Course> getListCourseTakenByUserId(Long studentId, Pageable pageable) {
        studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not " +
                "found with ID"));
        return courseRepository.findCoursesTakenByUserId(studentId, pageable);
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

    @Override
    public List<Course> getCoursesByKeyword(String keyword) {
        return courseRepository.findByKeyword(keyword);
    }

    @Override
    public List<Course> getListCourseByTime(Long weekDay, Set<Long> periods, Long userId) {
        List<CourseTime> courseTimes = courseTimeRepository.findAllByWeekDay(weekDay);
        List<Course> courseList = new ArrayList<>();
        for(CourseTime courseTime: courseTimes) {
            if(periods.containsAll(courseTime.getPeriods())) {
                Course course = courseTime.getCourse();
                User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User is not found with id"));
                Student student = user.getStudent();
                boolean isContain = course.getStudents().contains(student);
                // Lấy thời gian hiện tại dưới dạng timestamp
                Instant currentTimestamp = Instant.now();
                // Chuyển đổi Instant thành timestamp (miligiây)
                long timestamp = currentTimestamp.toEpochMilli();
                if(course.getBeginDate() > timestamp && !isContain) {
                    courseList.add(course);
                }
            }
        }
        return courseList;
    }

    @Override
    public List<Course> filterCourse(Long status, Long enrollStatus, Long weekday) {
        if(weekday != -1) {
            List<CourseTime> list = courseTimeRepository.findAllByWeekDay(weekday);
            List<Course> courseList = new ArrayList<>();
            for(CourseTime ct: list) {
                courseList.add(ct.getCourse());
            }
            if(status == 0) {
                courseList.removeIf(c -> !(c.getBeginDate() > Instant.now().toEpochMilli()));
            } else if(status == 1) {
                courseList.removeIf(c -> !(c.getBeginDate() <= Instant.now().toEpochMilli() && c.getEndDate() > Instant.now().toEpochMilli()));
            } else if(status == 2) {
                courseList.removeIf(c -> !(c.getEndDate() <= Instant.now().toEpochMilli()));
            } if(enrollStatus == 0) {
                courseList.removeIf(c -> !(c.getStudents().size() == 0));
            } else if(enrollStatus == 1) {
                courseList.removeIf(c -> !(c.getStudents().size() > 0));
            }
            return courseList;
        } else {
            List<Course> list = courseRepository.findAll();
            if(status == 0) {
                list.removeIf(c -> !(c.getBeginDate() > Instant.now().toEpochMilli()));
            } else if(status == 1) {
                list.removeIf(c -> !(c.getBeginDate() <= Instant.now().toEpochMilli() && c.getEndDate() > Instant.now().toEpochMilli()));
            } else if(status == 2) {
                list.removeIf(c -> !(c.getEndDate() <= Instant.now().toEpochMilli()));
            } if(enrollStatus == 0) {
                list.removeIf(c -> !(c.getStudents().size() == 0));
            } else if(enrollStatus == 1) {
                list.removeIf(c -> !(c.getStudents().size() > 0));
            }
            return list;
        }
    }


}
