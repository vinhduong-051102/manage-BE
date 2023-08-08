package com.example.manage.controller;

import com.example.manage.dto.ErrorResponseDto;
import com.example.manage.exception.AddressRequiredException;
import com.example.manage.exception.DuplicateContentException;
import com.example.manage.exception.NameRequiredException;
import com.example.manage.model.Student;
import com.example.manage.request.CancelCourseRequest;
import com.example.manage.request.CreateStudentRequest;
import com.example.manage.request.EnrollCourseRequest;
import com.example.manage.response.GetAllResponse;
import com.example.manage.response.GetStudentResponse;
import com.example.manage.service.DistrictService;
import com.example.manage.service.ProvinceService;
import com.example.manage.service.StudentService;
import com.example.manage.service.WardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentApiController {

    private final StudentService studentService;

    private final ProvinceService provinceService;

    private final DistrictService districtService;

    private final WardService wardService;

    @PostMapping("/new")
    public ResponseEntity<?> createStudent(@RequestBody CreateStudentRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(request));
        } catch (AddressRequiredException | NameRequiredException e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getListStudent(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<GetStudentResponse> list = new ArrayList<>();
        List<Student> studentList = studentService.getAllStudents(pageable).getContent();
        for (Student s :
                studentList) {
            list.add(
                    GetStudentResponse
                            .builder()
                            .address(s.getAddress())
                            .id(s.getId())
                            .name(s.getName())
                            .isActive(s.getUser().getIsActive())
                            .provinceCode(s.getProvinceCode())
                            .districtCode(s.getDistrictCode())
                            .wardCode(s.getWardCode())
                            .email(s.getEmail())
                            .locationString(
                                    provinceService.findProvinceByCode(s.getProvinceCode()).getFullName()
                                            + ", " + districtService.findDistrictByCode(s.getDistrictCode()).getFullName()
                                            + ", " + wardService.findWardByCode(s.getWardCode()).getFullName()
                                            + ", " + s.getAddress()
                            )
                            .build()
            );
        }
        GetAllResponse<GetStudentResponse> response = GetAllResponse
                .<GetStudentResponse>builder()
                .data(list)
                .currentPage(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalRecord((long) studentService.getAllStudent().size()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("")
    public ResponseEntity<GetAllResponse<GetStudentResponse>> getListStudentByName(@RequestParam(name = "page") int page,
                                                                                   @RequestParam(name = "size") int size,
                                                                                   @RequestParam(name = "keySearch") String keySearch
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<GetStudentResponse> list = new ArrayList<>();
        List<Student> studentList = studentService.getStudentsByKeyword(keySearch, pageable).getContent();
        for (Student s :
                studentList) {
            list.add(
                    GetStudentResponse
                            .builder()
                            .address(s.getAddress())
                            .id(s.getId())
                            .name(s.getName())
                            .isActive(s.getUser().getIsActive())
                            .provinceCode(s.getProvinceCode())
                            .districtCode(s.getDistrictCode())
                            .wardCode(s.getWardCode())
                            .email(s.getEmail())
                            .locationString(
                                    provinceService.findProvinceByCode(s.getProvinceCode()).getFullName()
                                            + ", " + districtService.findDistrictByCode(s.getDistrictCode()).getFullName()
                                            + ", " + wardService.findWardByCode(s.getWardCode()).getFullName()
                                            + ", " + s.getAddress()
                            )
                            .build()
            );
        }
        GetAllResponse<GetStudentResponse> response = GetAllResponse
                .<GetStudentResponse>builder()
                .data(list)
                .currentPage(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .totalRecord((long) studentService.getStudentsByKeyword(keySearch).size()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editStudent(@RequestBody Student student) {
        try {
            studentService.updateStudent(student);
            return ResponseEntity.status(HttpStatus.OK).body("Update successfully");
        } catch (NameRequiredException | AddressRequiredException |
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteStudent(@RequestParam(name = "id") Long id) {
        try {
            studentService.deleteStudent(id);
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
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {
        try {
            Student student = studentService.getStudentById(id);
            return ResponseEntity.status(HttpStatus.OK).body(student);
        } catch (IllegalArgumentException e) {
            ErrorResponseDto errorResponse =
                    new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListStudentByCourseId(@RequestParam(
            "courseId") Long id, @RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            List<GetStudentResponse> list = new ArrayList<>();
            List<Student> studentList = studentService.getStudentsEnrollCourseId(id, pageable).getContent();
            for (Student s :
                    studentList) {
                list.add(
                        GetStudentResponse
                                .builder()
                                .address(s.getAddress())
                                .id(s.getId())
                                .name(s.getName())
                                .isActive(s.getUser().getIsActive())
                                .provinceCode(s.getProvinceCode())
                                .districtCode(s.getDistrictCode())
                                .wardCode(s.getWardCode())
                                .email(s.getEmail())
                                .locationString(
                                        provinceService.findProvinceByCode(s.getProvinceCode()).getFullName()
                                                + ", " + districtService.findDistrictByCode(s.getDistrictCode()).getFullName()
                                                + ", " + wardService.findWardByCode(s.getWardCode()).getFullName()
                                                + ", " + s.getAddress()
                                )
                                .build()
                );
            }
            GetAllResponse<GetStudentResponse> response = GetAllResponse
                    .<GetStudentResponse>builder()
                    .data(list)
                    .currentPage(pageable.getPageNumber() + 1)
                    .pageSize(pageable.getPageSize())
                    .totalRecord((long) studentService.getStudentsEnrollCourseId(id).size()).build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
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

    @PostMapping("/enroll")
    public ResponseEntity<?> enrollCourse(@RequestBody EnrollCourseRequest request) {
        studentService.enrollCourse(request.getUserId(), request.getListCourseId());
        return ResponseEntity.status(HttpStatus.OK).body("Enroll " +
                "successfully");
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelCourse(@RequestBody CancelCourseRequest request) {
        try {
            studentService.cancelCourse(request.getUserId(), request.getCourseId());
            return ResponseEntity.status(HttpStatus.OK).body("Cancel " +
                    "successfully");
        } catch (IllegalArgumentException e) {
            ErrorResponseDto errorResponse =
                    new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestParam(name = "email") String email) {
        List<Student> studentList = studentService.getAllStudent();
        for (Student s :
                studentList) {
            if (Objects.equals(s.getEmail(), email)) {
                return ResponseEntity.ok(false);
            }
        }
        return ResponseEntity.ok(true);
    }
}
