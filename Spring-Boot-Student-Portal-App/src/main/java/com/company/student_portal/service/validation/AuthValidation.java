package com.company.student_portal.service.validation;

import com.company.student_portal.dto.AuthCreateRequestDto;
import com.company.student_portal.dto.ErrorDto;
import com.company.student_portal.utils.ValidateCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthValidation {
    private final ValidateCredentials validateCredentials;

    public ErrorDto validateUser(AuthCreateRequestDto dto) {
        if (dto == null) {
            return new ErrorDto("/user", "Request body cannot be null",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (dto.getPassword() == null || !validateCredentials.isPasswordValid(dto.getPassword())) {
            return new ErrorDto("/user/password",
                    "Password must be at least 8 chars, contain upper/lowercase, number, special char",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (dto.getEmail() == null || !isUniversityDomain(dto.getEmail())) {
            return new ErrorDto("/user/email",
                    "Email is invalid or domain not allowed",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (dto.getUsername() == null || !validateCredentials.isUsernameValid(dto.getUsername())) {
            return new ErrorDto("/user/username",
                    "Username is invalid",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (dto.getRole() == null) {
            return new ErrorDto("/user/role",
                    "Role cannot be null",
                    HttpStatus.BAD_REQUEST.value());
        }

        return null; // hammasi valid
    }


    private boolean isUniversityDomain(String email) {
        String allowedDomain = "@university.edu"; // universitet domeni
        return email != null && email.toLowerCase().endsWith(allowedDomain);
    }


}
