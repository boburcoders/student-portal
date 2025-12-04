package com.company.student_portal.service;

import com.company.student_portal.dto.AddressRequestDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.TeacherProfileResponseDto;
import com.company.student_portal.dto.TeacherUpdateDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Primary
public interface TeacherProfileService {
    HttpApiResponse<TeacherProfileResponseDto> updateTeacherAvatar(Long teacherId, MultipartFile avatar);

    HttpApiResponse<TeacherProfileResponseDto> updateAddress(Long teacherId, AddressRequestDto dto);

    HttpApiResponse<TeacherProfileResponseDto> updateTeacherProfile(Long teacherId, TeacherUpdateDto dto);

    HttpApiResponse<TeacherProfileResponseDto> getProfile(Long id);
}
