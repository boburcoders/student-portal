package com.company.student_portal.service.impl;

import com.company.student_portal.domain.Department;
import com.company.student_portal.domain.Program;
import com.company.student_portal.dto.ErrorDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.ProgramRequestDto;
import com.company.student_portal.dto.ProgramResponseDto;
import com.company.student_portal.repository.DepartmentRepository;
import com.company.student_portal.repository.ProgramRepository;
import com.company.student_portal.service.ProgramService;
import com.company.student_portal.service.mapper.ProgramMapper;
import com.company.student_portal.service.validation.ProgramValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {
    private final ProgramRepository programRepository;
    private final DepartmentRepository departmentRepository;
    private final ProgramMapper programMapper;
    private final ProgramValidation programValidation;


    @Override
    @Transactional
    public HttpApiResponse<ProgramResponseDto> createProgram(ProgramRequestDto dto, Long departmentId) {
        ErrorDto error = programValidation.validateProgram(dto);
        if (error != null) {
            return HttpApiResponse.<ProgramResponseDto>builder()
                    .success(false)
                    .message(error.getMessage())
                    .code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(error)
                    .build();
        }
        Department department = departmentRepository.findByIdAndDeletedAtIsNull(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id " + departmentId));

        Program program = programMapper.toEntity(dto);
        program.setDepartment(department);

        programRepository.save(program);

        return HttpApiResponse.<ProgramResponseDto>builder()
                .success(true)
                .message("Program created successfully")
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(programMapper.toDto(program))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public HttpApiResponse<ProgramResponseDto> getProgramById(Long programId) {
        Program program = programRepository.findByIdAndDeletedAtIsNull(programId)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id " + programId));

        return HttpApiResponse.<ProgramResponseDto>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(programMapper.toDto(program))
                .build();
    }

    @Override
    public HttpApiResponse<List<ProgramResponseDto>> getAllProgramByDepartmentId(Long departmentId) {
        departmentRepository.findByIdAndDeletedAtIsNull(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id " + departmentId));

        List<Program> programList = programRepository.findAllByDepartmentIdAndDeletedAtIsNullWithNative(departmentId);

        if (programList.isEmpty()) {
            return HttpApiResponse.<List<ProgramResponseDto>>builder()
                    .success(false)
                    .message("No programs found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return HttpApiResponse.<List<ProgramResponseDto>>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(programList.stream().map(programMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<ProgramResponseDto> updateProgramById(Long programId, ProgramRequestDto dto) {
        Program program = programRepository.findByIdAndDeletedAtIsNull(programId)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id " + programId));

        Program updatedProgram = programMapper.updateProgram(program, dto);
        programRepository.save(updatedProgram);

        return HttpApiResponse.<ProgramResponseDto>builder()
                .success(true)
                .message("Program updated successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(programMapper.toDto(updatedProgram))
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> deleteProgramById(Long programId) {
        Program program = programRepository.findByIdAndDeletedAtIsNull(programId)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id " + programId));

        program.setDeletedAt(LocalDateTime.now());
        programRepository.save(program);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("Program deleted successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }
}
