package com.company.student_portal.controller;

import com.company.student_portal.dto.*;
import com.company.student_portal.service.TeacherProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/teacher-profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherProfileController {
    private final TeacherProfileService service;

    @GetMapping("/{id}")
    public ResponseEntity<HttpApiResponse<TeacherProfileResponseDto>> getProfile(@PathVariable Long id) {
        HttpApiResponse<TeacherProfileResponseDto> response = service.getProfile(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value = "/update-avatar/{teacherId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpApiResponse<TeacherProfileResponseDto>> updateTeacherAvatar
            (@PathVariable Long teacherId,
             @RequestParam MultipartFile avatar
            ) {
        HttpApiResponse<TeacherProfileResponseDto> response = service.updateTeacherAvatar(teacherId, avatar);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value = "/update-address/{teacherId}")
    public ResponseEntity<HttpApiResponse<TeacherProfileResponseDto>> updateAddress(
            @PathVariable Long teacherId,
            @RequestBody AddressRequestDto dto
    ) {
        HttpApiResponse<TeacherProfileResponseDto> response = service.updateAddress(teacherId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value = "/update-student/{teacherId}")
    public ResponseEntity<HttpApiResponse<TeacherProfileResponseDto>> updateTeacherProfile(
            @PathVariable Long teacherId,
            @RequestBody TeacherUpdateDto dto
    ) {
        HttpApiResponse<TeacherProfileResponseDto> response = service.updateTeacherProfile(teacherId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
