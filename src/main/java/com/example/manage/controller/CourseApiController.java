package com.example.manage.controller;

import com.example.manage.dto.ErrorResponseDto;
import com.example.manage.exception.DescriptionRequiredException;
import com.example.manage.exception.DuplicateContentException;
import com.example.manage.exception.NameRequiredException;
import com.example.manage.model.Course;
import com.example.manage.model.Period;
import com.example.manage.request.CreateCourseRequest;
import com.example.manage.response.GetAllResponse;
import com.example.manage.response.GetCourseResponse;
import com.example.manage.service.CourseService;
import com.example.manage.service.PeriodService;
import com.example.manage.service.WeekDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseApiController {

    private final CourseService courseService;

    private final PeriodService periodService;

    private final WeekDayService weekDayService;

    @PostMapping("/new")
    public ResponseEntity<?> createCourse(@RequestBody CreateCourseRequest request) {
        courseService.createCourse(request);
        return ResponseEntity.ok("Create course successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCourse(@RequestParam(name = "size") int size, @RequestParam(value = "page") int page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<GetCourseResponse> list = new ArrayList<>();
        List<Course> courseList = courseService.getAllCourses(pageable).getContent();
        for (Course c :
                courseList) {
            List<Period> periodList = new ArrayList<>();
            for(Long id: c.getCourseTime().getPeriods()) {
                periodList.add(periodService.findById(id));
            }
            list.add(
                    GetCourseResponse
                            .builder()
                            .id(c.getId())
                            .name(c.getName())
                            .description(c.getDescription())
                            .weekDay(weekDayService.findByNo(c.getCourseTime().getWeekDay()))
                            .listPeriod(periodList)
                            .build()
            );
        }
        GetAllResponse<GetCourseResponse> response = GetAllResponse
                .<GetCourseResponse>builder()
                .data(list)
                .currentPage(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalRecord((long) courseService.getAllCourses().size())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editCourse(@RequestBody Course course) {
        try {
            courseService.updateCourse(course);
            return ResponseEntity.status(HttpStatus.OK).body("Update successfully");
        } catch (NameRequiredException | DescriptionRequiredException |
                 IllegalArgumentException e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (DuplicateContentException e) {
            ErrorResponseDto errorResponse =
                    new ErrorResponseDto(HttpStatus.NOT_MODIFIED.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deleteCourse(@PathVariable("id") Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
        } catch (NumberFormatException e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (IllegalArgumentException e) {
            ErrorResponseDto errorResponse =
                    new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getCourseById(@PathVariable("id") Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.status(HttpStatus.OK).body(course);
        } catch (IllegalArgumentException e) {
            ErrorResponseDto errorResponse =
                    new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getListCourseByStudentId(@RequestParam("studentId") Long id) {
        try {
            List<Course> courseList = courseService.getListCourseTakenByUserId(id);
            return ResponseEntity.status(HttpStatus.OK).body(courseList);
        } catch (NumberFormatException e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (IllegalArgumentException e) {
            ErrorResponseDto errorResponse =
                    new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/except")
    public ResponseEntity<?> getListCourseNotContainStudentId(@RequestParam(
            "studentId") Long id) {
        try {
            List<Course> courseList = courseService.getListCourseTakenByUserId(id);
            return ResponseEntity.status(HttpStatus.OK).body(courseList);
        } catch (NumberFormatException e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (IllegalArgumentException e) {
            ErrorResponseDto errorResponse =
                    new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
