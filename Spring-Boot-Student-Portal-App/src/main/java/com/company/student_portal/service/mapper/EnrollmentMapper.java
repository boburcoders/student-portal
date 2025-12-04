package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.Enrollment;
import com.company.student_portal.dto.EnrollmentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(target = "courseId", source = "e.course.id")
    @Mapping(target = "studentId", source = "e.student.id")
    EnrollmentResponseDto toDto(Enrollment e);
}
