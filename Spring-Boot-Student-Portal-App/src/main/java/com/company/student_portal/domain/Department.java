package com.company.student_portal.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Department extends BaseEntity {
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_id")
    private AuthUser headUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "university_id")
    private University university;

}
