package com.company.student_portal.controller;

import com.company.student_portal.dto.AssignmentResponseDto;
import com.company.student_portal.dto.AssignmentRequestDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignment")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;


    @PostMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<AssignmentResponseDto>> createAssignment(
            @PathVariable Long courseId,
            @RequestBody AssignmentRequestDto dto
    ) {
        HttpApiResponse<AssignmentResponseDto> response = assignmentService.createAssignment(courseId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<HttpApiResponse<AssignmentResponseDto>> getAssignmentById(
            @PathVariable Long assignmentId
    ) {
        HttpApiResponse<AssignmentResponseDto> response = assignmentService.getAssignmentById(assignmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<HttpApiResponse<List<AssignmentResponseDto>>> getAllAssignments() {
        HttpApiResponse<List<AssignmentResponseDto>> response = assignmentService.getAllAssignments();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-by-course/{courseId}")
    public ResponseEntity<HttpApiResponse<List<AssignmentResponseDto>>> getAllByCourseId(@PathVariable Long courseId) {
        HttpApiResponse<List<AssignmentResponseDto>> response = assignmentService.getAllByCourseId(courseId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{assignmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<AssignmentResponseDto>> updateAssignmentById(
            @PathVariable Long assignmentId,
            @RequestBody AssignmentRequestDto dto
    ) {
        HttpApiResponse<AssignmentResponseDto> response = assignmentService.updateAssignmentById(assignmentId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{assignmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteAssignmentById(@PathVariable Long assignmentId) {
        HttpApiResponse<Boolean> response = assignmentService.deleteAssignmentById(assignmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
