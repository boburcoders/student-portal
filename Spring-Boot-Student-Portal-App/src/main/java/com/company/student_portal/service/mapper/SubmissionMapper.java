package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.Submission;
import com.company.student_portal.dto.SubmissionResponseDto;
import com.company.student_portal.dto.SubmissionUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
    @Mapping(target = "assignmentId", source = "submission.assignment.id")
    @Mapping(target = "studentId", source = "submission.student.id")
    SubmissionResponseDto toDto(Submission submission);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Submission updateEntity(@MappingTarget Submission submission, SubmissionUpdateDto dto);
}
