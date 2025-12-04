package com.company.student_portal.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentProfileResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private Boolean isActive;
    private Boolean isPasswordReset;
    private String email;
    private String studentNumber;
    private String avatar;
    private Long addressId;
}
