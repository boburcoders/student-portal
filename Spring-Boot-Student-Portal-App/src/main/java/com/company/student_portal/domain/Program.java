package com.company.student_portal.domain;

import com.company.student_portal.domain.enums.DegreeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Program extends BaseEntity {
    private String name;
    private Integer durationYear;

    @Enumerated(EnumType.STRING)
    private DegreeType degreeType;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

}
