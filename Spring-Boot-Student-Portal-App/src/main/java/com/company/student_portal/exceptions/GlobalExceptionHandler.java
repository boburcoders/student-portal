package com.company.student_portal.exceptions;

import com.company.student_portal.dto.HttpApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        HttpApiResponse<Void> response = HttpApiResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException ex) {
        HttpApiResponse<Void> response = HttpApiResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .code(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        HttpApiResponse<Void> response = HttpApiResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED)
                .code(HttpStatus.UNAUTHORIZED.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
