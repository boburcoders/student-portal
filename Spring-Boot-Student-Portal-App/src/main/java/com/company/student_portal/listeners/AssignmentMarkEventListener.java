package com.company.student_portal.listeners;

import com.company.student_portal.events.AssignmentMarkEvent;
import com.company.student_portal.utils.u_service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AssignmentMarkEventListener {
    private final MailService mailService;

    @Async
    @EventListener(value = {AssignmentMarkEvent.class})
    public void handleAssignmentMarkEvent(AssignmentMarkEvent event) {
        log.info("Assignment Mark Event Received {}", event);
        mailService.assignmentMarkNotification(event.studentEmail(), event.submissionId());
    }
}
