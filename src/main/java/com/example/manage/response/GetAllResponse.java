package com.example.manage.response;

import com.example.manage.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllResponse<T> {
    private List<T> data;
    private Long totalRecord;
    private int currentPage;
    private int pageSize;
}
