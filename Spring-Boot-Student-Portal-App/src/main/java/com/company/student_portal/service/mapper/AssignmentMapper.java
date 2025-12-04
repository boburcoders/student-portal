package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.Assignment;
import com.company.student_portal.dto.AssignmentRequestDto;
import com.company.student_portal.dto.AssignmentResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {
    @Mapping(target = "course", ignore = true)
    Assignment toEntity(AssignmentRequestDto dto);

    @Mapping(target = "courseId", source = "result.course.id")
    AssignmentResponseDto toDto(Assignment result);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "course", ignore = true)
    Assignment updateEntity(@MappingTarget Assignment assignment, AssignmentRequestDto dto);
}
