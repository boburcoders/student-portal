package com.company.student_portal.controller;

import com.company.student_portal.dto.CourseRequestDto;
import com.company.student_portal.dto.CourseResponseDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/{programId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<CourseResponseDto>> createCourse(
            @PathVariable Long programId,
            @RequestBody CourseRequestDto dto) {
        HttpApiResponse<CourseResponseDto> response = courseService.createCourse(programId, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<HttpApiResponse<CourseResponseDto>> getCourseById(@PathVariable Long courseId) {
        HttpApiResponse<CourseResponseDto> response = courseService.getCourseById(courseId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get-all")
    public ResponseEntity<HttpApiResponse<List<CourseResponseDto>>> getAllCourse() {
        HttpApiResponse<List<CourseResponseDto>> response = courseService.getAllCourse();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("get-all-by-programId/{programId}")
    public ResponseEntity<HttpApiResponse<List<CourseResponseDto>>> getAllCourseByProgramId(@PathVariable Long programId) {
        HttpApiResponse<List<CourseResponseDto>> response = courseService.getAllCourseByProgramId(programId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<CourseResponseDto>> updateCourseById(@PathVariable Long id, @RequestBody CourseRequestDto dto) {
        HttpApiResponse<CourseResponseDto> response = courseService.updateCourseById(id, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/add-teacher")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> addTeacherToCourse
            (@RequestParam("teacherId") Long teacherId,
             @RequestParam("courseId") Long courseId) {
        HttpApiResponse<Boolean> response = courseService.addTeacherToCourse(teacherId, courseId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/remove-teacher")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> removeTeacherFromCourse
            (@RequestParam("teacherId") Long teacherId,
             @RequestParam("courseId") Long courseId) {
        HttpApiResponse<Boolean> response = courseService.removeTeacherFromCourse(teacherId, courseId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','HEAD')")
    public ResponseEntity<HttpApiResponse<Boolean>> deleteCourseById(@PathVariable Long id) {
        HttpApiResponse<Boolean> response = courseService.deleteCourseById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
