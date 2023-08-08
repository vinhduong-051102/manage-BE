package com.example.manage.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCourseRequest {
    private String name;
    private String shortDescription;
    private String detailDescription;
    private Set<Long> periods;
    private Long weekDay;
    private Long beginDate;
    private Long endDate;
}
