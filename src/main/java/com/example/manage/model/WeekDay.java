package com.example.manage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "week_day")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WeekDay {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private int no;
}
