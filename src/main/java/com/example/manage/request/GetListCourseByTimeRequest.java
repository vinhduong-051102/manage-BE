package com.example.manage.request;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetListCourseByTimeRequest {
    private Long userId;
    private Set<Long> time;
    private Long day;
}
