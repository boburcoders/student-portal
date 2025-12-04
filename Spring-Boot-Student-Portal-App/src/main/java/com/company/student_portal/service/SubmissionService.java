package com.company.student_portal.service;

import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.SubmissionResponseDto;
import com.company.student_portal.dto.SubmissionUpdateDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Primary
public interface SubmissionService {
    HttpApiResponse<SubmissionResponseDto> createSubmission(Long studentId, Long assignmentId, MultipartFile file);

    HttpApiResponse<SubmissionResponseDto> getSubmissionById(Long submissionId);

    HttpApiResponse<List<SubmissionResponseDto>> getSubmissionsByStudentId(Long studentId);

    HttpApiResponse<List<SubmissionResponseDto>> getSubmissionsByAssignmentId(Long assignmentId);

    HttpApiResponse<SubmissionResponseDto> updateSubmission(Long submissionId, SubmissionUpdateDto dto);

    HttpApiResponse<Boolean> deleteSubmission(Long submissionId);
}
