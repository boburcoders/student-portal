package com.company.student_portal.events;



public record ProgramEnrollmentCreatedEvent(String studentEmail, Long studentId, String programTitle, Long programId) {
}
