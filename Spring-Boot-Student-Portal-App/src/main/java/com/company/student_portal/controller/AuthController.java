package com.company.student_portal.controller;

import com.company.student_portal.dto.*;
import com.company.student_portal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<HttpApiResponse<AuthResponseDto>> getCurrentUser(Authentication authentication) {
        HttpApiResponse<AuthResponseDto> response = userService.getCurrentUser(authentication);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<HttpApiResponse<TokenResponse>> getAccessToken(
            @RequestBody TokenRequest request
    ) {
        HttpApiResponse<TokenResponse> response = userService.getAccessToken(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<HttpApiResponse<TokenResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        HttpApiResponse<TokenResponse> response =
                userService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    //faqat admin foydalana oladi
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<HttpApiResponse<AuthResponseDto>> createUser(
            @RequestBody AuthCreateRequestDto dto) {
        HttpApiResponse<AuthResponseDto> response = userService.registerUser(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value = "/upload-students", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> uploadStudents(
            @RequestParam("file") MultipartFile file) {
        HttpApiResponse<Boolean> response = userService.registerStudentByFile(file);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(value = "/upload-instructors", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> uploadInstructors(
            @RequestParam("file") MultipartFile file) {
        HttpApiResponse<Boolean> response = userService.registerInstructorByFile(file);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
