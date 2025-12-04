package com.company.student_portal.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherUpdateDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
}
