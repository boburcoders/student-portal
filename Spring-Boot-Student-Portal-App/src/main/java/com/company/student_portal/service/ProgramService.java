package com.company.student_portal.service;

import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.ProgramRequestDto;
import com.company.student_portal.dto.ProgramResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProgramService {
    HttpApiResponse<ProgramResponseDto> createProgram(ProgramRequestDto dto, Long departmentId);

    HttpApiResponse<ProgramResponseDto> getProgramById(Long programId);

    HttpApiResponse<List<ProgramResponseDto>> getAllProgramByDepartmentId(Long departmentId);

    HttpApiResponse<ProgramResponseDto> updateProgramById(Long programId, ProgramRequestDto dto);

    HttpApiResponse<Boolean> deleteProgramById(Long programId);
}
