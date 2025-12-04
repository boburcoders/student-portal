package com.company.student_portal.controller;

import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.SubmissionResponseDto;
import com.company.student_portal.dto.SubmissionUpdateDto;
import com.company.student_portal.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
@RequestMapping("/api/submission")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpApiResponse<SubmissionResponseDto>> createSubmission
            (
                    @RequestParam("studentId") Long studentId,
                    @RequestParam("assignmentId") Long assignmentId,
                    @RequestParam MultipartFile file
            ) {
        HttpApiResponse<SubmissionResponseDto> response = service.createSubmission(studentId, assignmentId, file);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/by-id/{submissionId}")
    public ResponseEntity<HttpApiResponse<SubmissionResponseDto>> getSubmissionById(@PathVariable("submissionId") Long submissionId) {
        HttpApiResponse<SubmissionResponseDto> response = service.getSubmissionById(submissionId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/by-studentId/{studentId}")
    public ResponseEntity<HttpApiResponse<List<SubmissionResponseDto>>> getSubmissionsByStudentId(@PathVariable("studentId") Long studentId) {
        HttpApiResponse<List<SubmissionResponseDto>> response = service.getSubmissionsByStudentId(studentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/by-assignmentId/{assignmentId}")
    public ResponseEntity<HttpApiResponse<List<SubmissionResponseDto>>> getSubmissionsByAssignmentId(@PathVariable("assignmentId") Long assignmentId) {
        HttpApiResponse<List<SubmissionResponseDto>> response = service.getSubmissionsByAssignmentId(assignmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{submissionId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<SubmissionResponseDto>> updateSubmission(
            @PathVariable("submissionId") Long submissionId,
            @RequestBody SubmissionUpdateDto dto) {
        HttpApiResponse<SubmissionResponseDto> response = service.updateSubmission(submissionId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @DeleteMapping("/{submissionId}")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteSubmission(@PathVariable("submissionId") Long submissionId) {
        HttpApiResponse<Boolean> response = service.deleteSubmission(submissionId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


}
