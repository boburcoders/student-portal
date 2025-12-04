package com.company.student_portal.controller;

import com.company.student_portal.dto.EnrollmentResponseDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<HttpApiResponse<EnrollmentResponseDto>> createEnrollment(
            @RequestParam("studentId") Long studentId,
            @RequestParam("courseId") Long courseId
    ) {
        HttpApiResponse<EnrollmentResponseDto> response = enrollmentService.createEnrollment(studentId, courseId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<HttpApiResponse<EnrollmentResponseDto>> getEnrollmentById(@PathVariable("id") Long id) {
        HttpApiResponse<EnrollmentResponseDto> response = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-by-student/{studentId}")
    public ResponseEntity<HttpApiResponse<List<EnrollmentResponseDto>>> getAllByStudentId(@PathVariable("studentId") Long studentId) {
        HttpApiResponse<List<EnrollmentResponseDto>> response = enrollmentService.getAllByStudentId(studentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-by-course/{courseId}")
    public ResponseEntity<HttpApiResponse<List<EnrollmentResponseDto>>> getAllByCourseId(@PathVariable("courseId") Long courseId) {
        HttpApiResponse<List<EnrollmentResponseDto>> response = enrollmentService.getAllByCourseId(courseId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> confirmEnrollment(
            @RequestParam("studentId") Long studentId,
            @RequestParam("courseId") Long courseId
    ) {
        HttpApiResponse<Boolean> response = enrollmentService.confirmEnrollment(studentId, courseId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteEnrollmentById(@PathVariable("id") Long id) {
        HttpApiResponse<Boolean> response = enrollmentService.deleteEnrollmentById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
