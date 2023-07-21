package com.example.manage.response;


import com.example.manage.model.CourseTime;
import com.example.manage.model.Period;
import com.example.manage.model.WeekDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCourseResponse {
    private Long id;
    private String name;
    private String description;
    private WeekDay weekDay;
    private List<Period> listPeriod;
}
