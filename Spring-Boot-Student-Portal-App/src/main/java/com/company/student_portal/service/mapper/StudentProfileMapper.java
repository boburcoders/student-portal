package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.StudentProfile;
import com.company.student_portal.dto.StudentProfileResponseDto;
import com.company.student_portal.dto.StudentProfileUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentProfileMapper {


    @Mapping(target = "addressId", source = "profile.address.id")
    StudentProfileResponseDto toDto(StudentProfile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    StudentProfile updateProfile(@MappingTarget StudentProfile profile, StudentProfileUpdateDto dto);
}
