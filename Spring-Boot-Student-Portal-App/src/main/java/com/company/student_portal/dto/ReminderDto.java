package com.company.student_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReminderDto {
    private Long assignmentId;
    private String title;
    private LocalDateTime deadline;
    private int daysLeft;
}
