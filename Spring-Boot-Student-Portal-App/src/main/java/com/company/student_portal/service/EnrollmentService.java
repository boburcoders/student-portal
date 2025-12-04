package com.company.student_portal.service;

import com.company.student_portal.dto.EnrollmentResponseDto;
import com.company.student_portal.dto.HttpApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EnrollmentService {
    HttpApiResponse<EnrollmentResponseDto> createEnrollment(Long studentId, Long courseId);

    HttpApiResponse<EnrollmentResponseDto> getEnrollmentById(Long id);

    HttpApiResponse<List<EnrollmentResponseDto>> getAllByStudentId(Long studentId);

    HttpApiResponse<List<EnrollmentResponseDto>> getAllByCourseId(Long courseId);

    HttpApiResponse<Boolean> deleteEnrollmentById(Long id);

    HttpApiResponse<Boolean> confirmEnrollment(Long studentId, Long courseId);

    void createEnrollmentByProgramId(Long studentId, Long programId);

    void confirmCourseEnrollmentByProgramId(Long studentId, Long programId);
}
