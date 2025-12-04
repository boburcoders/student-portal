package com.company.student_portal.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpApiResponse<T> {
    private boolean success;
    private String message;
    private int code;
    private HttpStatus status;
    private T data;
    private ErrorDto error;
}
