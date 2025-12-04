package com.company.student_portal.service.validation;

import com.company.student_portal.dto.DepartmentRequestDto;
import com.company.student_portal.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DepartmentValidation {
    public ErrorDto validateDepartmentName(DepartmentRequestDto dto) {
        String name = dto.getName();
        if (name == null || name.isEmpty()) {
            return new ErrorDto(
                    "/department",
                    "Department name must not be null or empty",
                    HttpStatus.BAD_REQUEST.value()
            );
        }
        return null;
    }
}
