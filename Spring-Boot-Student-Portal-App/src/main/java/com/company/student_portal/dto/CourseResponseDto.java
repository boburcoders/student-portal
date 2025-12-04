package com.company.student_portal.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponseDto {
    private Long id;
    private String code;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private Long programId;
    private Long teacherId;
}
