package com.company.student_portal.service;

import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.ProgramEnrollmentResponseDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public interface ProgramEnrollmentService {
    HttpApiResponse<ProgramEnrollmentResponseDto> createEnrollment(Long studentId, Long programId);

    HttpApiResponse<ProgramEnrollmentResponseDto> getEnrollmentById(Long id);

    HttpApiResponse<ProgramEnrollmentResponseDto> getByStudentId(Long studentId);

    HttpApiResponse<List<ProgramEnrollmentResponseDto>> getAllByProgramId(Long programId);

    HttpApiResponse<Boolean> confirmEnrollment(Long studentId, Long programEnrollmentId);

    HttpApiResponse<Boolean> deleteEnrollmentById(Long id);
}
