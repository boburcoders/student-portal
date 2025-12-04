package com.company.student_portal.controller;

import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.ProgramRequestDto;
import com.company.student_portal.dto.ProgramResponseDto;
import com.company.student_portal.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/program")
@RequiredArgsConstructor
public class ProgramController {
    private final ProgramService programService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<ProgramResponseDto>> createProgram(
            @RequestBody ProgramRequestDto dto,
            @RequestParam("departmentId") Long departmentId) {
        HttpApiResponse<ProgramResponseDto> response = programService.createProgram(dto, departmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<HttpApiResponse<ProgramResponseDto>> getProgramById(@RequestParam("programId") Long programId) {
        HttpApiResponse<ProgramResponseDto> response = programService.getProgramById(programId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-all-by-departmentId")
    public ResponseEntity<HttpApiResponse<List<ProgramResponseDto>>> getAllProgramByDepartmentId(@RequestParam("departmentId") Long departmentId) {
        HttpApiResponse<List<ProgramResponseDto>> response = programService.getAllProgramByDepartmentId(departmentId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<ProgramResponseDto>> updateProgramById(
            @RequestParam("programId") Long programId,
            @RequestBody ProgramRequestDto dto) {
        HttpApiResponse<ProgramResponseDto> response = programService.updateProgramById(programId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteProgramById(@RequestParam("programId") Long programId) {
        HttpApiResponse<Boolean> response = programService.deleteProgramById(programId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
