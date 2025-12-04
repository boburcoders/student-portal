package com.company.student_portal.controller;

import com.company.student_portal.dto.AddressRequestDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.StudentProfileResponseDto;
import com.company.student_portal.dto.StudentProfileUpdateDto;
import com.company.student_portal.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/student-profile")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
public class StudentProfileController {
    private final StudentProfileService service;

    @GetMapping("/{id}")
    public ResponseEntity<HttpApiResponse<StudentProfileResponseDto>> getProfile(@PathVariable("id") Long id) {
        HttpApiResponse<StudentProfileResponseDto> response = service.getProfile(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value = "/update-avatar/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpApiResponse<StudentProfileResponseDto>> updateStudentProfileAvatar(
            @PathVariable Long studentId,
            @RequestParam MultipartFile avatar
    ) {
        HttpApiResponse<StudentProfileResponseDto> response = service.updateStudentProfileAvatar(studentId, avatar);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value = "/update-address/{studentId}")
    public ResponseEntity<HttpApiResponse<StudentProfileResponseDto>> updateAddress(
            @PathVariable Long studentId,
            @RequestBody AddressRequestDto dto
    ) {
        HttpApiResponse<StudentProfileResponseDto> response = service.updateAddress(studentId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(value = "/update-student/{studentId}")
    public ResponseEntity<HttpApiResponse<StudentProfileResponseDto>> updateStudentProfile(
            @PathVariable Long studentId,
            @RequestBody StudentProfileUpdateDto dto
    ) {
        HttpApiResponse<StudentProfileResponseDto> response = service.updateStudentProfile(studentId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
