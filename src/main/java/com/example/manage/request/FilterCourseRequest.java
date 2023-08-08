package com.example.manage.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FilterCourseRequest {
    private Long status;
    private Long enrollStatus;
    private Long weekday;
}
