package com.company.student_portal.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmissionResponseDto {
    private Long id;
    private String feedback;
    private Double score;
    private String fileUrl;
    private Long studentId;
    private Long assignmentId;
}
