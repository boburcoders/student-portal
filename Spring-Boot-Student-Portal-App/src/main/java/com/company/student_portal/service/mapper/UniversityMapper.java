package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.University;
import com.company.student_portal.dto.UniversityRequestDto;
import com.company.student_portal.dto.UniversityResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UniversityMapper {

    University toEntity(UniversityRequestDto dto);

    UniversityResponseDto toDto(University savedUniversity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    University updateUniversity(@MappingTarget University university, UniversityRequestDto dto);
}
