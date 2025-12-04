package com.company.student_portal.service.validation;

import com.company.student_portal.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TeacherProfileValidation {
    public ErrorDto validateAvatar(MultipartFile avatar) {
        if (avatar.isEmpty()) {
            return new ErrorDto("/teacherProfile", "Teacher avatar must not be empty or null", HttpStatus.BAD_REQUEST.value());
        }
        if (avatar.getSize() >= 5 * (1024 * 1024)) {
            return new ErrorDto("/teacherProfile", "Teacher avatar must not be greater than 5 Mb", HttpStatus.BAD_REQUEST.value());
        }
        return null;
    }
}
