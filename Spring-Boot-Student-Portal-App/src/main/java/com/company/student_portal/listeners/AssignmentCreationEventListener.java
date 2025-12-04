package com.company.student_portal.listeners;

import com.company.student_portal.dto.payloads.NotificationPayload;
import com.company.student_portal.events.AssignmentCreationEvent;
import com.company.student_portal.service.AssignmentService;
import com.company.student_portal.service.impl.NotificationService;
import com.company.student_portal.utils.u_service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssignmentCreationEventListener {
    private final MailService mailService;
    private final AssignmentService assignmentService;
    private final NotificationService notificationService;

    @Async
    @EventListener(value = {AssignmentCreationEvent.class})
    public void handleAssignmentCreationEvent(AssignmentCreationEvent event) {
        NotificationPayload payload = new NotificationPayload();
        payload.setSubject("Assignment Creation");
        payload.setBody("New Assignment Created Assigment title is " + event.getAssignmentTitle());
        payload.setAssignmentId(event.getCourseId());
        log.info("Assignment creation event received");
        mailService.sendAssignmentCreationMail(event);
        notificationService.sendNotificationToUser(event.getUsername(), payload);


    }
}
