package com.company.student_portal.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Submission extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String feedback;

    private Double score;

    @Column(columnDefinition = "TEXT")
    private String fileUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private StudentProfile student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

}
