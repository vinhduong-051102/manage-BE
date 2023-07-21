package com.example.manage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class StudentCourseKey implements Serializable {

    @Column(name = "student_id")
    Long studentId;

    @Column(name = "course_id")
    Long courseId;

}

@Entity
@Table(name = "student_course")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentCourse {
    @EmbeddedId
    private StudentCourseKey id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @MapsId("student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @MapsId("course_id")
    private Course course;

}