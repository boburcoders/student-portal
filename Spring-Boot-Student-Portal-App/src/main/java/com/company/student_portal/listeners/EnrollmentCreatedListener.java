package com.company.student_portal.listeners;

import com.company.student_portal.events.ProgramEnrollmentCreatedEvent;
import com.company.student_portal.service.EnrollmentService;
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
public class EnrollmentCreatedListener {
    private final MailService mailService;
    private final EnrollmentService enrollmentService;
    private final NotificationService notificationService;

    @Async
    @EventListener(value = {ProgramEnrollmentCreatedEvent.class})
    public void enrollmentCreated(ProgramEnrollmentCreatedEvent event) {
        log.info("ProgramEnrollment Created Event listened");
        notificationService.enrollmentCreateNotification(event);
        enrollmentService.createEnrollmentByProgramId(event.studentId(), event.programId());
        mailService.sendEnrollmentCreateNotification(event);
    }


}
