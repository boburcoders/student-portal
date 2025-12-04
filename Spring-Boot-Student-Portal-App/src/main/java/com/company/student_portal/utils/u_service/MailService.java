package com.company.student_portal.utils.u_service;

import com.company.student_portal.events.AssignmentCreationEvent;
import com.company.student_portal.events.ProgramEnrollmentCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendHtmlMail(String to, String subject, String htmlBody);

    void notifyTeacherForSubmit(String studentEmail, String teacherEmail, Long assignmentId);

    void assignmentMarkNotification(String studentEmail, Long submissionId);

    void sendEnrollmentCreateNotification(ProgramEnrollmentCreatedEvent event);

    void sendProgramEnrollmentConfirmation(Long studentId, Long programId);

    void sendAssignmentCreationMail(AssignmentCreationEvent event);
}
