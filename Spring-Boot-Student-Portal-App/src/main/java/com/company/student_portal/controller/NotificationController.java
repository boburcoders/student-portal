package com.company.student_portal.controller;

import com.company.student_portal.dto.NotificationRequest;
import com.company.student_portal.service.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // REST endpoint orqali notification yuborish
    @PostMapping("/send")
    public Map<String, String> sendNotification(@RequestBody NotificationRequest request) {
        System.out.println("📨 Received notification request");
        System.out.println("👤 Username: " + request.getUsername());
        System.out.println("💬 Message: " + request.getMessage());

        // Message object dan title va text olish
        if (request.getMessage() != null) {
            Map<String, String> messageMap = (Map<String, String>) request.getMessage();
            String title = messageMap.get("title");
            String text = messageMap.get("text");

            System.out.println("📋 Title: " + title);
            System.out.println("📝 Text: " + text);

            // Service ga yuborish
            notificationService.sendNotificationToUser(
                    request.getUsername(),
                    request.getMessage()
            );
        }

        return Map.of(
                "status", "success",
                "message", "Notification sent to " + request.getUsername()
        );
    }
}
