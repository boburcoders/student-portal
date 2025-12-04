package com.company.student_portal.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class AssignmentCreationEvent extends ApplicationEvent {
    private Long courseId;
    private String assignmentTitle;
    private String courseTitle;
    private LocalDateTime deadline;
    private String username;

    public AssignmentCreationEvent(Object source, Long courseId, String assignmentTitle, String courseTitle, LocalDateTime deadline, String username) {
        super(source);
        this.courseId = courseId;
        this.assignmentTitle = assignmentTitle;
        this.courseTitle = courseTitle;
        this.deadline = deadline;
        this.username = username;
    }
}
