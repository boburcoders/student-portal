package com.company.student_portal.service;

import com.company.student_portal.dto.CourseRequestDto;
import com.company.student_portal.dto.CourseResponseDto;
import com.company.student_portal.dto.HttpApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {
    HttpApiResponse<CourseResponseDto> createCourse(Long programId, CourseRequestDto dto);

    HttpApiResponse<CourseResponseDto> getCourseById(Long courseId);

    HttpApiResponse<List<CourseResponseDto>> getAllCourse();

    HttpApiResponse<List<CourseResponseDto>> getAllCourseByProgramId(Long programId);

    HttpApiResponse<CourseResponseDto> updateCourseById(Long id, CourseRequestDto dto);

    HttpApiResponse<Boolean> deleteCourseById(Long id);

    HttpApiResponse<Boolean> addTeacherToCourse(Long teacherId, Long courseId);

    HttpApiResponse<Boolean> removeTeacherFromCourse(Long teacherId, Long courseId);
}
