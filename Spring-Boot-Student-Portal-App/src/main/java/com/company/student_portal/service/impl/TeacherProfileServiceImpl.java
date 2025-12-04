package com.company.student_portal.service.impl;

import com.company.student_portal.domain.Address;
import com.company.student_portal.domain.TeacherProfile;
import com.company.student_portal.dto.*;
import com.company.student_portal.repository.AddressRepository;
import com.company.student_portal.repository.TeacherProfileRepository;
import com.company.student_portal.service.TeacherProfileService;
import com.company.student_portal.service.mapper.AddressMapper;
import com.company.student_portal.service.mapper.TeacherProfileMapper;
import com.company.student_portal.service.validation.TeacherProfileValidation;
import com.company.student_portal.utils.ValidateCredentials;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TeacherProfileServiceImpl implements TeacherProfileService {
    private final TeacherProfileRepository teacherProfileRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final TeacherProfileMapper teacherProfileMapper;
    private final ValidateCredentials validateCredentials;
    private final TeacherProfileValidation teacherProfileValidation;


    @Override
    public HttpApiResponse<TeacherProfileResponseDto> getProfile(Long id) {
        TeacherProfile teacherProfile = teacherProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher Profile Not Found with id: " + id));

        return HttpApiResponse.<TeacherProfileResponseDto>builder()
                .success(true)
                .message("OK")
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(teacherProfileMapper.toDto(teacherProfile))
                .build();
    }

    @Override
    public HttpApiResponse<TeacherProfileResponseDto> updateTeacherAvatar(Long teacherId, MultipartFile avatar) {
        var error = teacherProfileValidation.validateAvatar(avatar);
        if (error != null) {
            return HttpApiResponse.<TeacherProfileResponseDto>builder()
                    .success(false)
                    .message(error.getMessage())
                    .code(error.getCode())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(error)
                    .build();
        }
        TeacherProfile teacherProfile = teacherProfileRepository.findByIdAndDeletedAtIsNull(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id " + teacherId));
        // S3 will make URL
        teacherProfile.setAvatar("URl");
        teacherProfileRepository.save(teacherProfile);

        return HttpApiResponse.<TeacherProfileResponseDto>builder()
                .success(true)
                .message("Teacher avatar updated successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(teacherProfileMapper.toDto(teacherProfile))
                .build();
    }

    @Override
    public HttpApiResponse<TeacherProfileResponseDto> updateAddress(Long teacherId, AddressRequestDto dto) {
        TeacherProfile teacherProfile = teacherProfileRepository.findByIdAndDeletedAtIsNull(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id " + teacherId));
        if (teacherProfile.getAddress() != null) {
            teacherProfile.getAddress().setDeletedAt(LocalDateTime.now());

            Address entity = addressMapper.toEntity(dto);
            Address savedAddress = addressRepository.save(entity);

            teacherProfile.setAddress(savedAddress);
        } else {
            var mapperEntity = addressMapper.toEntity(dto);
            Address saved = addressRepository.save(mapperEntity);
            teacherProfile.setAddress(saved);
        }
        teacherProfileRepository.save(teacherProfile);

        return HttpApiResponse.<TeacherProfileResponseDto>builder()
                .success(true)
                .message("Teacher address updated successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(teacherProfileMapper.toDto(teacherProfile))
                .build();
    }

    @Override
    public HttpApiResponse<TeacherProfileResponseDto> updateTeacherProfile(Long teacherId, TeacherUpdateDto dto) {
        var teacherProfile = teacherProfileRepository.findByIdAndDeletedAtIsNull(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id " + teacherId));
        if (!validateCredentials.isPasswordValid(dto.getPassword())) {
            throw new IllegalArgumentException("Password is invalid");
        }

        var updateProfile = teacherProfileMapper.updateProfile(teacherProfile, dto);
        teacherProfileRepository.save(updateProfile);

        return HttpApiResponse.<TeacherProfileResponseDto>builder()
                .success(true)
                .message("Teacher profile updated successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(teacherProfileMapper.toDto(teacherProfile))
                .build();
    }
}
