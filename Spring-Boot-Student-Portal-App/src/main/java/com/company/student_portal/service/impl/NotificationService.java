package com.company.student_portal.service.impl;

import com.company.student_portal.domain.AuthUser;
import com.company.student_portal.domain.Notification;
import com.company.student_portal.domain.enums.NotificationType;
import com.company.student_portal.dto.payloads.NotificationPayload;
import com.company.student_portal.events.ProgramEnrollmentCreatedEvent;
import com.company.student_portal.repository.AuthRepository;
import com.company.student_portal.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final AuthRepository authRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public void enrollmentCreateNotification(ProgramEnrollmentCreatedEvent event) {
        AuthUser authUser = authRepository.findByEmailAndDeletedAtIsNull(event.studentEmail())
                .orElseThrow(() -> new EntityNotFoundException("Student Not Found with given email: " + event.studentEmail()));
        Notification notification = new Notification();
        notification.setTitle(event.programTitle());
        notification.setRecipient(authUser);
        notification.setType(NotificationType.ENROLLMENT_PENDING);
        notificationRepository.save(notification);

    }

    // Bitta userga notification yuborish
    public void sendNotificationToUser(String username, Object notification) {
        simpMessagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                notification
        );
    }

    // Barcha userlarga (topic orqali)
    public void sendNotificationToAll(Object notification) {
        simpMessagingTemplate.convertAndSend(
                "/topic/announcements",
                notification
        );
    }

    public void sendNotificationWithRest(String username, String message) {
        sendNotificationToUser(username, message);
    }
}
