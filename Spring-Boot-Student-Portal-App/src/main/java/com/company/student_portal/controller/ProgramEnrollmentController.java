package com.company.student_portal.controller;

import com.company.student_portal.dto.EnrollmentResponseDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.ProgramEnrollmentResponseDto;
import com.company.student_portal.service.EnrollmentService;
import com.company.student_portal.service.ProgramEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/program-enrollment")
@RequiredArgsConstructor
public class ProgramEnrollmentController {

    private final ProgramEnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<ProgramEnrollmentResponseDto>> createEnrollment(
            @RequestParam("studentId") Long studentId,
            @RequestParam("programId") Long programId
    ) {
        HttpApiResponse<ProgramEnrollmentResponseDto> response = enrollmentService.createEnrollment(studentId, programId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<HttpApiResponse<ProgramEnrollmentResponseDto>> getEnrollmentById(@PathVariable("id") Long id) {
        HttpApiResponse<ProgramEnrollmentResponseDto> response = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-by-student/{studentId}")
    public ResponseEntity<HttpApiResponse<ProgramEnrollmentResponseDto>> getByStudentId(@PathVariable("studentId") Long studentId) {
        HttpApiResponse<ProgramEnrollmentResponseDto> response = enrollmentService.getByStudentId(studentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-by-course/{programId}")
    public ResponseEntity<HttpApiResponse<List<ProgramEnrollmentResponseDto>>> getAllByProgramId(@PathVariable("programId") Long programId) {
        HttpApiResponse<List<ProgramEnrollmentResponseDto>> response = enrollmentService.getAllByProgramId(programId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> confirmEnrollment(
            @RequestParam("studentId") Long studentId,
            @RequestParam("programEnrollmentId") Long programEnrollmentId
    ) {
        HttpApiResponse<Boolean> response = enrollmentService.confirmEnrollment(studentId, programEnrollmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteEnrollmentById(@PathVariable("id") Long id) {
        HttpApiResponse<Boolean> response = enrollmentService.deleteEnrollmentById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
