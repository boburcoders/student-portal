package com.company.student_portal.service.validation;

import com.company.student_portal.dto.AssignmentRequestDto;
import com.company.student_portal.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AssignmentValidation {
    public ErrorDto validateAssignment(AssignmentRequestDto dto) {
        if (dto.getTitle() == null) {
            return new ErrorDto("/assignment", "Assignment title must not be null", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getDescription() == null) {
            return new ErrorDto("/assignment", "Assignment description must not be null", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getDeadline() == null) {
            return new ErrorDto("/assignment", "Assignment deadline must not be null", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getMaxScore() <= 0) {
            return new ErrorDto("/assignment", "Max score must not be less than or equal zero", HttpStatus.BAD_REQUEST.value());
        }

        if (dto.getMaxScore() > 100) {
            return new ErrorDto("/assignment", "Max score must not be grater than hundred", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getMaxScore() == null) {
            return new ErrorDto("/assignment", "Max score must not be null", HttpStatus.BAD_REQUEST.value());
        }
        return null;
    }
}
