package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.TeacherProfile;
import com.company.student_portal.dto.TeacherProfileResponseDto;
import com.company.student_portal.dto.TeacherUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TeacherProfileMapper {
    @Mapping(target = "addressId", source = "profile.address.id")
    TeacherProfileResponseDto toDto(TeacherProfile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TeacherProfile updateProfile(@MappingTarget TeacherProfile teacherProfile, TeacherUpdateDto dto);
}
