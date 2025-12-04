package com.company.student_portal.service.validation;

import com.company.student_portal.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StudentProfileValidation {
    public ErrorDto validateAvatar(MultipartFile avatar) {
        if (avatar.isEmpty()) {
            return new ErrorDto("/studentProfile", "Profile avatar must not be null", HttpStatus.BAD_REQUEST.value());
        }
        if(avatar.getSize()>(1024*1024)*5) {
            return new ErrorDto("/studentProfile", "Profile avatar size must be less than 5 MB", HttpStatus.BAD_REQUEST.value());
        }
        return null;
    }
}
