package com.company.student_portal.schedular;

import com.company.student_portal.domain.Assignment;
import com.company.student_portal.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AssignmentDeadlineSchedular {
    private final AssignmentRepository assignmentRepository;

    public void checkDeadlineReminders() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime fiveDays = now.plusDays(5);
        sendReminder(fiveDays, 5);

        LocalDateTime threeDays = now.plusDays(3);
        sendReminder(threeDays, 5);

        LocalDateTime oneDay = now.plusDays(1);
        sendReminder(oneDay, 5);
    }

    private void sendReminder(LocalDateTime targetDay, int daysLeft) {
        List<Assignment> assignmentList = assignmentRepository
                .findAllByDeletedAtIsNullAndDeadline(targetDay);

    }

}
