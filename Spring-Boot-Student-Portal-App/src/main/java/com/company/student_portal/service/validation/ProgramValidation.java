package com.company.student_portal.service.validation;

import com.company.student_portal.dto.ErrorDto;
import com.company.student_portal.dto.ProgramRequestDto;
import com.company.student_portal.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProgramValidation {
    private final ProgramRepository programRepository;

    public ErrorDto validateProgram(ProgramRequestDto dto) {
        Objects.requireNonNull(dto.getName(), "Name is required");
        boolean exists = programRepository.existsByNameAndDeletedAtIsNull(dto.getName());
        if (exists) {
            return new ErrorDto("/program", "Program already exists", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getDegreeType() == null) {
            return new ErrorDto("/degreeType", "Degree type is required", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getDurationYear() == null || dto.getDurationYear() <= 0) {
            return new ErrorDto("/durationYears",
                    "Duration years is required and it should be grater than 0", HttpStatus.BAD_REQUEST.value());
        }
        return null;
    }
}
