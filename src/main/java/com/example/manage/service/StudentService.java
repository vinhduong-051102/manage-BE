package com.example.manage.service;

import com.example.manage.model.Student;
import com.example.manage.request.CreateStudentRequest;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface StudentService {
    Page<Student> getAllStudents(Pageable pageable);

    List<Student> getAllStudent();

    Student createStudent(CreateStudentRequest request) throws MessagingException;

    Student updateStudent(Student student);

    void deleteStudent(Long id);

    Student getStudentById(Long id);

    void enrollCourse(Long studentId, Long courseId);

    void cancelCourse(Long studentId, Long courseId);

    List<Student> getStudentsEnrollCourseId(Long courseId);
    Page<Student> getStudentsByKeyword(String keyword, Pageable pageable);
    List<Student> getStudentsByKeyword(String keyword);

}
