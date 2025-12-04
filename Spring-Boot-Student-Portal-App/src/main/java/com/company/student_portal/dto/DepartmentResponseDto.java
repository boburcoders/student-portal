package com.company.student_portal.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentResponseDto {
    private Long id;
    private String name;
    private Long headId;
    private Long universityId;
    private LocalDateTime createdAt;
}
