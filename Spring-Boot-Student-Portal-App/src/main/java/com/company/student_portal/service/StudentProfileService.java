package com.company.student_portal.service;

import com.company.student_portal.dto.AddressRequestDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.StudentProfileResponseDto;
import com.company.student_portal.dto.StudentProfileUpdateDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Primary
public interface StudentProfileService {
    HttpApiResponse<StudentProfileResponseDto> updateStudentProfileAvatar(Long studentId, MultipartFile avatar);

    HttpApiResponse<StudentProfileResponseDto> updateAddress(Long studentId, AddressRequestDto dto);

    HttpApiResponse<StudentProfileResponseDto> updateStudentProfile(Long studentId, StudentProfileUpdateDto dto);

    HttpApiResponse<StudentProfileResponseDto> getProfile(Long id);
}
