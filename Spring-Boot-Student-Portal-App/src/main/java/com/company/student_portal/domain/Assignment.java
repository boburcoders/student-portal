package com.company.student_portal.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Assignment extends BaseEntity {
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime deadline;
    private Double maxScore;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;
}
