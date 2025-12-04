package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.Course;
import com.company.student_portal.dto.CourseRequestDto;
import com.company.student_portal.dto.CourseResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "program", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    Course toEntity(CourseRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "programId", source = "course.program.id")
    @Mapping(target = "teacherId", source = "course.teacher.id")
    CourseResponseDto toDto(Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "program", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    Course updateCourse(@MappingTarget Course course, CourseRequestDto dto);
}
