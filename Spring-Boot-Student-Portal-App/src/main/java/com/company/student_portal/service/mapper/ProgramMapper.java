package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.Program;
import com.company.student_portal.dto.ProgramRequestDto;
import com.company.student_portal.dto.ProgramResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProgramMapper {
    @Mapping(target = "department", ignore = true)
    Program toEntity(ProgramRequestDto dto);

    @Mapping(target = "departmentId", source = "program.department.id")
    ProgramResponseDto toDto(Program program);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "degreeType", ignore = true)
    Program updateProgram(@MappingTarget Program program, ProgramRequestDto dto);
}
