package com.company.student_portal.events;


public record ProgramEnrollmentConfirmedEvent(Long studentId, Long programEnrollmentId, Long programId) {
}
