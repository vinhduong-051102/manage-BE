package com.example.manage.service.impl;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.manage.exception.*;
import com.example.manage.model.*;
import com.example.manage.repository.CourseRepository;
import com.example.manage.repository.StudentCourseRepository;
import com.example.manage.repository.StudentRepository;
import com.example.manage.repository.UserRepository;
import com.example.manage.request.CreateStudentRequest;
import com.example.manage.service.AuthenticationService;
import com.example.manage.service.StudentService;
import com.example.manage.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    private final StudentCourseRepository studentCourseRepository;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final PasswordEncoder passwordEncoder;

    public Page<Student> getAllStudents(@NotNull Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public Student createStudent(@NotNull CreateStudentRequest request) throws MessagingException {
        if(request.getUsername().isEmpty()) {
            throw new NameRequiredException("Student name must have value");
        }
        if(request.getAddress().isEmpty()) {
            throw new DescriptionRequiredException("Student address must have value");
        }
        Student student = studentRepository.save(
                Student
                        .builder()
                        .name(request.getUsername())
                        .email(request.getEmail())
                        .address(request.getAddress())
                        .provinceCode(request.getProvinceCode())
                        .districtCode(request.getDistrictCode())
                        .wardCode(request.getWardCode())
                        .build()
        );
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        int len = 10;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        User user = User.builder()
                .role(ERole.STUDENT)
                .email(request.getEmail())
                .password(sb.toString())
                .student(student)
                .isActive(false)
                .build();
        authenticationService.register(user);
        return student;
    }

    public Student updateStudent(@NotNull Student student) {
        if(student.getName().isEmpty()) {
            throw new NameRequiredException("Student name must have value");
        }
        if(student.getAddress().isEmpty()) {
            throw new AddressRequiredException("Student address must have value");
        }
        Student s = studentRepository.findById(student.getId())
                .orElseThrow(
                        () -> new UsernameNotFoundException("Student not found")
                );
        s.setEmail(student.getEmail());
        s.setName(student.getName());
        s.setProvinceCode(student.getProvinceCode());
        s.setDistrictCode(student.getDistrictCode());
        s.setWardCode(student.getWardCode());
        s.setAddress(student.getAddress());
        return studentRepository.save(s);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not " +
                        "found with ID: " + id));

        List<Course> courses = student.getCourses();
        for (Course course : courses) {
            studentCourseRepository.deleteByStudentIdAndCourseId(id,
                    course.getId());
        }
        studentRepository.delete(student);
    }

    @Override
    public Student getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElseThrow(() -> new IllegalArgumentException(
                "Student" +
                " not " +
                "found with ID: " + id));
    }

    @Override
    public void enrollCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);
        Optional<StudentCourse> studentCourse = studentCourseRepository.findEnrollCourse(studentId,
                        courseId);
        if(studentCourse.isPresent()) {
            throw new IllegalArgumentException("This course is already " +
                    "enrolled");
        }
        if (student == null || course == null) {
            throw new IllegalArgumentException("User or course not found");
        }
        List<Course> studentCourses = student.getCourses();
        studentCourses.add(course);
        student.setCourses(studentCourses);
        studentRepository.save(student);

        List<Student> courseStudents = course.getStudents();
        courseStudents.add(student);
        course.setStudents(courseStudents);
        courseRepository.save(course);
    }

    @Override
    public void cancelCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);
        if (student == null || course == null) {
            throw new IllegalArgumentException("User or course not found");
        }
        studentCourseRepository.findEnrollCourse(studentId, courseId).orElseThrow(() -> new IllegalArgumentException("This course is not enrolled"));
        List<Course> studentCourses = student.getCourses();
        studentCourses.remove(course);
        student.setCourses(studentCourses);
        studentRepository.save(student);

        List<Student> courseStudents = course.getStudents();
        courseStudents.remove(student);
        course.setStudents(courseStudents);
        courseRepository.save(course);

    }
    @Override
    public List<Student> getStudentsEnrollCourseId(Long courseId) {
        courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException(
                "Course not found with ID"));
        return studentRepository.findStudentsEnrollCourseId(courseId).orElse(new ArrayList<>());
    }
    @Override
    public Page<Student> getStudentsByKeyword(String keyword,
                                              @NotNull Pageable pageable) {
        List<Student> students = studentRepository.findAll();
        int totalStudent = students.size();
        int pageSize = pageable.getPageSize();
        int maxPages = (totalStudent + pageSize - 1) / pageSize;
        if(maxPages < pageable.getPageNumber()) {
            throw new PageNumberToLargeException("Page number to large, please select between 1 to " + maxPages);
        }
        return studentRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public List<Student> getStudentsByKeyword(String keyword) {
        return studentRepository.findByKeyword(keyword);
    }
}
