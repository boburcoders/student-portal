package com.company.student_portal.service.impl;

import com.company.student_portal.domain.StudentProfile;
import com.company.student_portal.dto.*;
import com.company.student_portal.repository.AddressRepository;
import com.company.student_portal.repository.StudentProfileRepository;
import com.company.student_portal.service.StudentProfileService;
import com.company.student_portal.service.mapper.AddressMapper;
import com.company.student_portal.service.mapper.StudentProfileMapper;
import com.company.student_portal.service.validation.StudentProfileValidation;
import com.company.student_portal.utils.ValidateCredentials;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {
    private final StudentProfileRepository studentProfileRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final StudentProfileMapper studentProfileMapper;
    private final ValidateCredentials validateCredentials;
    private final StudentProfileValidation studentProfileValidation;

    @Override
    public HttpApiResponse<StudentProfileResponseDto> getProfile(Long id) {
        StudentProfile profile = studentProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found with id " + id));

        return HttpApiResponse.<StudentProfileResponseDto>builder()
                .success(true)
                .message("OK")
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(studentProfileMapper.toDto(profile))
                .build();
    }

    @Override
    public HttpApiResponse<StudentProfileResponseDto> updateStudentProfileAvatar(Long studentId, MultipartFile avatar) {
        ErrorDto error = studentProfileValidation.validateAvatar(avatar);
        if (error != null) {
            return HttpApiResponse.<StudentProfileResponseDto>builder()
                    .success(false)
                    .message(error.getMessage())
                    .code(error.getCode())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(error)
                    .build();
        }
        StudentProfile profile = studentProfileRepository.findByIdAndDeletedAtIsNull(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with given id: " + studentId));

        // S3 will make URL
        profile.setAvatar("URL");
        studentProfileRepository.save(profile);

        return HttpApiResponse.<StudentProfileResponseDto>builder()
                .success(true)
                .message("Successfully updated student profile")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(studentProfileMapper.toDto(profile))
                .build();
    }

    @Override
    public HttpApiResponse<StudentProfileResponseDto> updateAddress(Long studentId, AddressRequestDto dto) {
        StudentProfile profile = studentProfileRepository.findByIdAndDeletedAtIsNull(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with given id: " + studentId));
        if (profile.getAddress() == null) {
            var entity = addressMapper.toEntity(dto);
            var savedRepository = addressRepository.save(entity);
            profile.setAddress(savedRepository);
        } else {
            profile.getAddress().setDeletedAt(LocalDateTime.now());
            var entity = addressMapper.toEntity(dto);
            var savedRepository = addressRepository.save(entity);
            profile.setAddress(savedRepository);
        }
        studentProfileRepository.save(profile);
        return HttpApiResponse.<StudentProfileResponseDto>builder()
                .success(true)
                .message("Successfully updated student profile")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(studentProfileMapper.toDto(profile))
                .build();
    }

    @Override
    public HttpApiResponse<StudentProfileResponseDto> updateStudentProfile(Long studentId, StudentProfileUpdateDto dto) {
        StudentProfile profile = studentProfileRepository.findByIdAndDeletedAtIsNull(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with given id: " + studentId));
        if (!validateCredentials.isPasswordValid(dto.getPassword())) {
            throw new IllegalArgumentException("Password is not valid");
        }
        var updatedProfile = studentProfileMapper.updateProfile(profile, dto);

        var savedProfile = studentProfileRepository.save(updatedProfile);

        return HttpApiResponse.<StudentProfileResponseDto>builder()
                .success(true)
                .message("Successfully updated student profile")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(studentProfileMapper.toDto(savedProfile))
                .build();
    }
}
