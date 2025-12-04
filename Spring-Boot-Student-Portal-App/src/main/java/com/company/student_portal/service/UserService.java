package com.company.student_portal.service;

import com.company.student_portal.dto.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {
    HttpApiResponse<AuthResponseDto> registerUser(AuthCreateRequestDto dto);

    HttpApiResponse<Boolean> registerStudentByFile(MultipartFile file);

    HttpApiResponse<Boolean> registerInstructorByFile(MultipartFile file);

    HttpApiResponse<TokenResponse> getAccessToken(TokenRequest request);

    HttpApiResponse<TokenResponse> refreshAccessToken(String refreshToken);

    HttpApiResponse<AuthResponseDto> getCurrentUser(Authentication authentication);
}
