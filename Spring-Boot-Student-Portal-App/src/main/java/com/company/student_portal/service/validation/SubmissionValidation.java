package com.company.student_portal.service.validation;

import com.company.student_portal.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class SubmissionValidation {
    public ErrorDto validateSubmission(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ErrorDto("/submission", "Submission file must not be empty or null", HttpStatus.BAD_REQUEST.value());
        }
        if (file.getSize() > 5 * (1024 * 1024)) {
            return new ErrorDto("/submission", "Submission file must not be grater than 5 Mb", HttpStatus.BAD_REQUEST.value());
        }
        return null;
    }
}
