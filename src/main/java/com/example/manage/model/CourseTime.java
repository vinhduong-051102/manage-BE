package com.example.manage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Table(name = "course_time")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CourseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private Set<Long> periods;

    @Column(name = "week_day")
    private Long weekDay;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

}
