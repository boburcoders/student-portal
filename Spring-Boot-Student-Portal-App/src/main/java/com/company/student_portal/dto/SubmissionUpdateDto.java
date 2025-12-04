package com.company.student_portal.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmissionUpdateDto {
    private String feedback;
    private Double score;
}
