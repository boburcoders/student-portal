package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.AuthUser;
import com.company.student_portal.domain.StudentProfile;
import com.company.student_portal.domain.TeacherProfile;
import com.company.student_portal.dto.AuthCreateRequestDto;
import com.company.student_portal.dto.AuthResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "password",ignore = true)
    AuthUser toEntity(AuthCreateRequestDto dto);


    @Mapping(target = "isPasswordReset", source = "user.isPasswordReset")
    @Mapping(target = "isActive", source = "user.isActive")
    @Mapping(target = "passwordHash", ignore = true)
    AuthResponseDto toDto(AuthUser user);

    @Mapping(target = "address",ignore = true)
    @Mapping(target = "avatar",ignore = true)
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "studentNumber",ignore = true)
    StudentProfile mapToStudentProfile(AuthCreateRequestDto dto);

    @Mapping(target = "address",ignore = true)
    @Mapping(target = "avatar",ignore = true)
    @Mapping(target = "password",ignore = true)
    TeacherProfile mapToTeacherProfile(AuthCreateRequestDto dto);
}
