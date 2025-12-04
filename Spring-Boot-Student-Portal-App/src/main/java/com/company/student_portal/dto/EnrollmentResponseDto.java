package com.company.student_portal.dto;

import com.company.student_portal.domain.enums.EnrollmentStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentResponseDto {
    private Long id;
    private EnrollmentStatus status;
    private Long studentId;
    private Long courseId;
}
