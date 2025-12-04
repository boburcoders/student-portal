package com.company.student_portal.listeners;

import com.company.student_portal.events.ProgramEnrollmentConfirmedEvent;
import com.company.student_portal.service.EnrollmentService;
import com.company.student_portal.utils.u_service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgramEnrollmentConfirmedEventListener {
    private final EnrollmentService enrollmentService;
    private final MailService mailService;

    @Async
    @EventListener(value = {ProgramEnrollmentConfirmedEvent.class})
    public void onProgramEnrollmentConfirmedEvent(ProgramEnrollmentConfirmedEvent event) {
        enrollmentService.confirmCourseEnrollmentByProgramId(event.studentId(), event.programId());
        mailService.sendProgramEnrollmentConfirmation(event.studentId(), event.programId());
    }
}
