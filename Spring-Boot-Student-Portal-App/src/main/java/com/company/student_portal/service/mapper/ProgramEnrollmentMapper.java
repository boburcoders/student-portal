package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.ProgramEnrollment;
import com.company.student_portal.dto.ProgramEnrollmentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgramEnrollmentMapper {
    @Mapping(target = "programId", source = "saved.program.id")
    @Mapping(target = "studentId", source = "saved.student.id")
    ProgramEnrollmentResponseDto toDto(ProgramEnrollment saved);
}
