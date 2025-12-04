package com.company.student_portal.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Double maxScore;
    private Long courseId;
    private LocalDateTime createdAt;
}
