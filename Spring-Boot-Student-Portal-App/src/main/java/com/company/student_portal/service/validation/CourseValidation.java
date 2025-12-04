package com.company.student_portal.service.validation;

import com.company.student_portal.dto.CourseRequestDto;
import com.company.student_portal.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CourseValidation {
    public ErrorDto validateCourse(CourseRequestDto dto) {
        if (dto.getTitle() == null) {
            return new ErrorDto("/course", "Course title must not be null", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getDescription() == null) {
            return new ErrorDto("/course", "Course title must not be null", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getCode() == null) {
            return new ErrorDto("/course", "Course title must not be null", HttpStatus.BAD_REQUEST.value());
        }
        return null;
    }
}
