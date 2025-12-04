package com.company.student_portal.dto;

import com.company.student_portal.domain.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthCreateRequestDto {
    private String username;
    private String email;
    private String password;
    private UserRole role;
}
