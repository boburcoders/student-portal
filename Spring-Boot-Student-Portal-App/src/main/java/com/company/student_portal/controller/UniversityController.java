package com.company.student_portal.controller;

import com.company.student_portal.dto.AddressRequestDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.UniversityRequestDto;
import com.company.student_portal.dto.UniversityResponseDto;
import com.company.student_portal.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/university")
@RequiredArgsConstructor
public class UniversityController {
    private final UniversityService universityService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpApiResponse<UniversityResponseDto>> createUniversity(@RequestBody UniversityRequestDto dto) {
        HttpApiResponse<UniversityResponseDto> response = universityService.createUniversity(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpApiResponse<UniversityResponseDto>> getUniversityById(@PathVariable Long id) {
        HttpApiResponse<UniversityResponseDto> response = universityService.getUniversityById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HEAD')")
    public ResponseEntity<HttpApiResponse<UniversityResponseDto>> updateUniversity(
            @PathVariable Long id,
            @RequestBody UniversityRequestDto dto) {
        HttpApiResponse<UniversityResponseDto> response = universityService.updateUniversity(id, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteUniversityById(@PathVariable Long id) {
        HttpApiResponse<Boolean> response = universityService.deleteUniversityById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PutMapping("/{id}/rector")
    @PreAuthorize("hasAnyRole('ADMIN','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> addOrUpdateRector(
            @PathVariable Long id,
            @RequestParam Long userId) {
        HttpApiResponse<Boolean> response = universityService.addOrUpdateUniversityRector(userId, id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}/address")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> addOrUpdateAddress(
            @PathVariable Long id,
            @RequestBody AddressRequestDto dto) {
        HttpApiResponse<Boolean> response = universityService.addOrUpdateUniversityAddress(dto, id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
