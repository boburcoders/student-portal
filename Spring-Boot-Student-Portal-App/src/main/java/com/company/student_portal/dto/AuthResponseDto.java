package com.company.student_portal.dto;

import com.company.student_portal.domain.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDto {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private boolean isActive;
    private boolean isPasswordReset;
    private UserRole role;
}
