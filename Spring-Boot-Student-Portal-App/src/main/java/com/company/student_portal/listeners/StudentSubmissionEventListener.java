package com.company.student_portal.listeners;

import com.company.student_portal.events.StudentSubmissionEvent;
import com.company.student_portal.utils.u_service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StudentSubmissionEventListener {
    private final MailService mailService;

    @Async
    @EventListener(value = {StudentSubmissionEvent.class})
    public void handleAssignmentEvent(StudentSubmissionEvent event) {
        log.info("Student submission event received: {}", event);
        mailService.notifyTeacherForSubmit(event.studentEmail(), event.teacherEmail(), event.assignmentId());
    }
}
