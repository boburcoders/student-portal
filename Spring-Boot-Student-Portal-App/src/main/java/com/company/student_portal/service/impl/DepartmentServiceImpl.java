package com.company.student_portal.service.impl;

import com.company.student_portal.domain.AuthUser;
import com.company.student_portal.domain.Department;
import com.company.student_portal.domain.University;
import com.company.student_portal.domain.enums.UserRole;
import com.company.student_portal.dto.*;
import com.company.student_portal.repository.DepartmentRepository;
import com.company.student_portal.repository.AuthRepository;
import com.company.student_portal.repository.UniversityRepository;
import com.company.student_portal.service.DepartmentService;
import com.company.student_portal.service.mapper.DepartmentMapper;
import com.company.student_portal.service.validation.DepartmentValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UniversityRepository universityRepository;
    private final AuthRepository userRepository;
    private final DepartmentMapper departmentMapper;
    private final DepartmentValidation departmentValidation;

    @Override
    public HttpApiResponse<DepartmentResponseDto> createDepartment(Long universityId, DepartmentRequestDto dto) {
        University university = universityRepository.findByIdAndDeletedAtIsNull(universityId)
                .orElseThrow(() -> new EntityNotFoundException("university not found with id: " + universityId));

        ErrorDto error = departmentValidation.validateDepartmentName(dto);
        if (error != null) {
            return HttpApiResponse.<DepartmentResponseDto>builder()
                    .success(false)
                    .message(error.getMessage())
                    .code(error.getCode())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(error)
                    .build();
        }
        Department entity = departmentMapper.toEntity(dto);
        entity.setUniversity(university);
        departmentRepository.save(entity);

        return HttpApiResponse.<DepartmentResponseDto>builder()
                .success(true)
                .message("Department Created")
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(departmentMapper.toDto(entity))
                .build();
    }

    @Override
    public HttpApiResponse<DepartmentResponseDto> addHeadUser(Long userId, Long departmentId) {
        AuthUser user = userRepository.findByIdAndIsActiveTrueAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        if (user.getRole() == UserRole.STUDENT)
            throw new IllegalArgumentException("Student is not allowed to add head department");
        boolean exist = departmentRepository.existByHeadUserNative(userId);
        if (exist) {
            throw new IllegalArgumentException("Head User already exist");
        }

        Department department = departmentRepository.findByIdAndDeletedAtIsNull(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));

        department.setHeadUser(user);
        departmentRepository.save(department);

        return HttpApiResponse.<DepartmentResponseDto>builder()
                .success(true)
                .message("Department Added Successfully")
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(departmentMapper.toDto(department))
                .build();
    }

    @Override
    public HttpApiResponse<DepartmentResponseDto> getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findByIdAndDeletedAtIsNull(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));

        var responseDto = departmentMapper.toDto(department);

        return HttpApiResponse.<DepartmentResponseDto>builder()
                .success(true)
                .message("OK")
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(responseDto)
                .build();
    }


    @Override
    public HttpApiResponse<List<DepartmentResponseDto>> getAllDepartmentUniversityId(Long universityId) {
        University university = universityRepository.findByIdAndDeletedAtIsNull(universityId)
                .orElseThrow(() -> new EntityNotFoundException("University not found with id: " + universityId));

        List<Department> departmentList = departmentRepository.findAllByUniversityIdNative(universityId);
        if (departmentList.isEmpty()) {
            return HttpApiResponse.<List<DepartmentResponseDto>>builder()
                    .success(false)
                    .message("Department not found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return HttpApiResponse.<List<DepartmentResponseDto>>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(departmentList.stream().map(departmentMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<List<DepartmentResponseDto>> getAllDepartment() {
        List<Department> departmentList = departmentRepository.findAllByDeletedAtIsNull();
        if (departmentList.isEmpty()) {
            return HttpApiResponse.<List<DepartmentResponseDto>>builder()
                    .success(false)
                    .message("Department List is Empty")
                    .code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return HttpApiResponse.<List<DepartmentResponseDto>>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(departmentList.stream().map(departmentMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<DepartmentResponseDto> updateDepartment(Long departmentId, DepartmentRequestDto dto) {
        Department department = departmentRepository.findByIdAndDeletedAtIsNull(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));

        departmentMapper.updateDepartment(department, dto);

        departmentRepository.save(department);

        return HttpApiResponse.<DepartmentResponseDto>builder()
                .success(true)
                .message("Department Updated")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(departmentMapper.toDto(department))
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findByIdAndDeletedAtIsNull(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + departmentId));

        department.setDeletedAt(LocalDateTime.now());

        departmentRepository.save(department);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("Department Deleted Successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }
}
