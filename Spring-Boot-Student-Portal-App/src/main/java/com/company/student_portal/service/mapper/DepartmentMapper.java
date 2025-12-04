package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.Department;
import com.company.student_portal.dto.DepartmentRequestDto;
import com.company.student_portal.dto.DepartmentResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = {ProgramMapper.class})
public interface DepartmentMapper {

    @Mapping(target = "headUser", ignore = true)
    @Mapping(target = "university", ignore = true)
    Department toEntity(DepartmentRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "headId", source = "headUser.id")
    @Mapping(target = "universityId", source = "university.id")
    DepartmentResponseDto toDto(Department entity);


    @Mapping(target = "headUser", ignore = true)
    @Mapping(target = "university", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartment(@MappingTarget Department department, DepartmentRequestDto dto);
}
