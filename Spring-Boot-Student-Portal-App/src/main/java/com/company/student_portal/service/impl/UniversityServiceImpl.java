package com.company.student_portal.service.impl;

import com.company.student_portal.domain.AuthUser;
import com.company.student_portal.domain.University;
import com.company.student_portal.domain.enums.UserRole;
import com.company.student_portal.dto.*;
import com.company.student_portal.repository.AddressRepository;
import com.company.student_portal.repository.AuthRepository;
import com.company.student_portal.repository.UniversityRepository;
import com.company.student_portal.service.UniversityService;
import com.company.student_portal.service.mapper.AddressMapper;
import com.company.student_portal.service.mapper.UniversityMapper;
import com.company.student_portal.service.validation.AddressValidation;
import com.company.student_portal.service.validation.UniversityValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;
    private final AuthRepository authRepository;
    private final AddressRepository addressRepository;
    private final UniversityMapper universityMapper;
    private final AddressMapper addressMapper;
    private final AddressValidation addressValidation;
    private final UniversityValidation universityValidation;


    @Override
    public HttpApiResponse<UniversityResponseDto> createUniversity(UniversityRequestDto dto) {
        var error = universityValidation.validateUniversity(dto);
        if (error != null) {
            return HttpApiResponse.<UniversityResponseDto>builder()
                    .success(false)
                    .message(error.getMessage())
                    .code(error.getCode())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(error)
                    .build();
        }
        var entity = universityMapper.toEntity(dto);
        var savedUniversity = universityRepository.save(entity);

        return HttpApiResponse.<UniversityResponseDto>builder()
                .success(true)
                .message("University created successfully")
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(universityMapper.toDto(savedUniversity))
                .build();
    }

    @Override
    public HttpApiResponse<UniversityResponseDto> getUniversityById(Long id) {
        University university = universityRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("University not found with given id: " + id));

        return HttpApiResponse.<UniversityResponseDto>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(universityMapper.toDto(university))
                .build();
    }

    @Override
    public HttpApiResponse<UniversityResponseDto> updateUniversity(Long id, UniversityRequestDto dto) {
        University university = universityRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("University not found with given id: " + id));
        var updatedEntity = universityMapper.updateUniversity(university, dto);
        universityRepository.save(updatedEntity);

        return HttpApiResponse.<UniversityResponseDto>builder()
                .success(true)
                .message("University updated successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(universityMapper.toDto(updatedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> deleteUniversityById(Long id) {
        University university = universityRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("University not found with given id: " + id));
        university.setDeletedAt(LocalDateTime.now());

        universityRepository.save(university);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("University deleted successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> addOrUpdateUniversityRector(Long userId, Long id) {
        AuthUser user = authRepository.findByIdAndIsActiveTrueAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with given id: " + id));
        if (user.getRole() == UserRole.STUDENT) {
            throw new IllegalArgumentException("Student is not allowed");
        }

        University university = universityRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("University not found with given id: " + id));

        boolean exist = universityRepository.existsByRectorNative(userId);
        if (exist) {
            throw new IllegalArgumentException("Rector is already exist");
        }
        university.setRector(user);
        universityRepository.save(university);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("Rector added successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> addOrUpdateUniversityAddress(AddressRequestDto dto, Long id) {
        University university = universityRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("University not found with given id: " + id));

        var error = addressValidation.validateAddress(dto);

        if (error != null) {
            return HttpApiResponse.<Boolean>builder()
                    .success(false)
                    .message(error.getMessage())
                    .code(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(false)
                    .error(error)
                    .build();
        }
        var entity = addressMapper.toEntity(dto);
        var savedAddress = addressRepository.save(entity);

        university.setAddress(savedAddress);

        universityRepository.save(university);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("Address added successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }
}
