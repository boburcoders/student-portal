package com.company.student_portal.service;

import com.company.student_portal.dto.DepartmentRequestDto;
import com.company.student_portal.dto.DepartmentResponseDto;
import com.company.student_portal.dto.HttpApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService {
    HttpApiResponse<DepartmentResponseDto> createDepartment(Long universityId,DepartmentRequestDto dto);

    HttpApiResponse<DepartmentResponseDto> getDepartmentById(Long departmentId);

    HttpApiResponse<List<DepartmentResponseDto>> getAllDepartment();

    HttpApiResponse<DepartmentResponseDto> updateDepartment(Long departmentId, DepartmentRequestDto dto);

    HttpApiResponse<Boolean> deleteDepartment(Long departmentId);

    HttpApiResponse<DepartmentResponseDto> addHeadUser(Long userId, Long departmentId);

    HttpApiResponse<List<DepartmentResponseDto>> getAllDepartmentUniversityId(Long universityId);
}
