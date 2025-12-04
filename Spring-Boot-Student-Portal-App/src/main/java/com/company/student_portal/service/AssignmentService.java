package com.company.student_portal.service;

import com.company.student_portal.dto.AssignmentResponseDto;
import com.company.student_portal.dto.AssignmentRequestDto;
import com.company.student_portal.dto.HttpApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AssignmentService {
    HttpApiResponse<AssignmentResponseDto> createAssignment(Long courseId, AssignmentRequestDto dto);

    HttpApiResponse<AssignmentResponseDto> getAssignmentById(Long assignmentId);

    HttpApiResponse<List<AssignmentResponseDto>> getAllAssignments();

    HttpApiResponse<List<AssignmentResponseDto>> getAllByCourseId(Long courseId);

    HttpApiResponse<AssignmentResponseDto> updateAssignmentById(Long assignmentId, AssignmentRequestDto dto);

    HttpApiResponse<Boolean> deleteAssignmentById(Long assignmentId);
}
