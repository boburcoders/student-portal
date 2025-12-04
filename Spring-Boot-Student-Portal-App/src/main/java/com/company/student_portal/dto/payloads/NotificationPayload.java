package com.company.student_portal.dto.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPayload {
    private String subject;
    private String body;
    private Long assignmentId;
}
