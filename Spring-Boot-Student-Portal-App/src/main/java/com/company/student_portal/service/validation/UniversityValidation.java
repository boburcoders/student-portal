package com.company.student_portal.service.validation;

import com.company.student_portal.dto.ErrorDto;
import com.company.student_portal.dto.UniversityRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UniversityValidation {
    public ErrorDto validateUniversity(UniversityRequestDto dto) {
        if (dto.getName() == null) {
            return new ErrorDto("/university/create", "University name must not be null", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getDescription() == null) {
            return new ErrorDto("/university/create", "University description must not be null", HttpStatus.BAD_REQUEST.value());
        }
        return null;
    }
}
