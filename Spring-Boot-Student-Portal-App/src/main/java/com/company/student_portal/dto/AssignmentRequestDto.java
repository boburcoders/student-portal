package com.company.student_portal.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentRequestDto {
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Double maxScore;
}
