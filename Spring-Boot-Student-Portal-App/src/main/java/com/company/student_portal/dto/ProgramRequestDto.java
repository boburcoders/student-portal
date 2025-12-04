package com.company.student_portal.dto;

import com.company.student_portal.domain.enums.DegreeType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramRequestDto {
    private String name;
    private DegreeType degreeType;
    private Integer durationYear;
}
