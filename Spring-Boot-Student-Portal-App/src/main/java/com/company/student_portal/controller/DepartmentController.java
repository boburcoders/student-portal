package com.company.student_portal.controller;

import com.company.student_portal.dto.DepartmentRequestDto;
import com.company.student_portal.dto.DepartmentResponseDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HEAD')")
    public ResponseEntity<HttpApiResponse<DepartmentResponseDto>> createDepartment(
            @RequestParam("universityId") Long universityId,
            @RequestBody DepartmentRequestDto dto) {
        HttpApiResponse<DepartmentResponseDto> response = departmentService.createDepartment(universityId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/add-head-user")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<HttpApiResponse<DepartmentResponseDto>> addHeadUser(
            @RequestParam("userId") Long userId,
            @RequestParam("departmentId") Long departmentId) {
        HttpApiResponse<DepartmentResponseDto> response = departmentService.addHeadUser(userId, departmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<HttpApiResponse<DepartmentResponseDto>> getDepartmentById(@RequestParam("departmentId") Long departmentId) {
        HttpApiResponse<DepartmentResponseDto> response = departmentService.getDepartmentById(departmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<HttpApiResponse<List<DepartmentResponseDto>>> getAllDepartment() {
        HttpApiResponse<List<DepartmentResponseDto>> response = departmentService.getAllDepartment();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-by-universityId/{universityId}")
    public ResponseEntity<HttpApiResponse<List<DepartmentResponseDto>>> getAllDepartmentByUniversityId(@PathVariable Long universityId) {
        HttpApiResponse<List<DepartmentResponseDto>> response = departmentService.getAllDepartmentUniversityId(universityId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','HEAD')")
    public ResponseEntity<HttpApiResponse<DepartmentResponseDto>> updateDepartment(
            @RequestParam("departmentId") Long departmentId,
            @RequestBody DepartmentRequestDto dto) {
        HttpApiResponse<DepartmentResponseDto> response = departmentService.updateDepartment(departmentId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteDepartment(
            @RequestParam("departmentId") Long departmentId) {
        HttpApiResponse<Boolean> response = departmentService.deleteDepartment(departmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
