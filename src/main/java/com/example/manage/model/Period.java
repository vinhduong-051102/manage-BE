package com.example.manage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Period {
    @Id
    private Long id;

    @Column
    private int period;

    @Column(name = "begin_at")
    private String beginAt;

    @Column(name = "end_at")
    private String endAt;
}
