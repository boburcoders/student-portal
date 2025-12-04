package com.company.student_portal.events;


public record StudentSubmissionEvent(String studentEmail, String teacherEmail, Long assignmentId) {
}
