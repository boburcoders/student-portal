package com.company.student_portal.dto;

import com.company.student_portal.domain.enums.DegreeType;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramResponseDto {
    private Long id;
    private String name;
    private DegreeType degreeType;
    private Integer durationYear;
    private Long departmentId;
}
