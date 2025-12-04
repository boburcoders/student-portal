package com.company.student_portal.dto;

import lombok.Data;

@Data
public class TokenRequest {
    private String username;
    private String password;
}
