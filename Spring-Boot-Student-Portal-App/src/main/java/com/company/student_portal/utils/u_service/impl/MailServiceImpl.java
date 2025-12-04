package com.company.student_portal.utils.u_service.impl;

import com.company.student_portal.domain.Course;
import com.company.student_portal.domain.Enrollment;
import com.company.student_portal.domain.Program;
import com.company.student_portal.domain.StudentProfile;
import com.company.student_portal.events.AssignmentCreationEvent;
import com.company.student_portal.events.ProgramEnrollmentCreatedEvent;
import com.company.student_portal.exceptions.EmailSendException;
import com.company.student_portal.repository.*;
import com.company.student_portal.utils.u_service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final CourseRepository courseRepository;
    private final StudentProfileRepository studentRepository;
    private final ProgramRepository programRepository;
    private final EnrollmentRepository enrollmentRepository;


    @Override
    public void sendHtmlMail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setTo(to);
            helper.setFrom("noreply@yourdomain.com");
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException("Failed to send email", e);
        }
    }

    @Override
    public void notifyTeacherForSubmit(String studentEmail, String teacherEmail, Long assignmentId) {
        String html = """
                <h2>New Assignment Submission</h2>
                <p>Student with email: <b>%s</b> submitted assignment ID: <b>%s</b>.</p>
                """.formatted(studentEmail, assignmentId);

        sendHtmlMail(teacherEmail, "New Submission Received", html);
    }

    @Override
    public void assignmentMarkNotification(String studentEmail, Long submissionId) {
        String html = """
                <h2>Your Assignment Has Been Marked</h2>
                <p>Your submission ID: <b>%s</b> has been evaluated.</p>
                """.formatted(submissionId);

        sendHtmlMail(studentEmail, "Assignment Mark Notification", html);
    }


    @Override
    public void sendEnrollmentCreateNotification(ProgramEnrollmentCreatedEvent event) {
        String programTitle = event.programTitle();
        String html = """
                <h2>Your Enrollment Has Been sent</h2>
                <p>Your Enrollment name: <b>%s</b> has been evaluated.</p>
                """.formatted(programTitle);
        sendHtmlMail(event.studentEmail(), "Enrollment Created", html);
    }

    @Override
    public void sendProgramEnrollmentConfirmation(Long studentId, Long programId) {

        // 1. Studentni olish
        StudentProfile student = studentRepository.findByIdAndDeletedAtIsNull(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        // 2. Programni olish
        Program program = programRepository.findByIdAndDeletedAtIsNull(programId)
                .orElseThrow(() -> new EntityNotFoundException("Program not found"));

        // 3. Program ichidagi kurslar
        List<Course> courseList = courseRepository.findAllCourseByProgramIdNative(programId);

        // 4. Course ro‘yxatini HTML listga aylantirish
        StringBuilder courseHtmlList = new StringBuilder("<ul>");
        for (Course course : courseList) {
            courseHtmlList.append("<li>").append(course.getTitle()).append("</li>");
        }
        courseHtmlList.append("</ul>");

        // 5. HTML email template
        String html = """
                <h2>Your Program Enrollment Has Been Confirmed</h2>
                <p>Congratulations <b>%s</b>, your enrollment request has been approved.</p>
                
                <p><b>Program:</b> %s</p>
                
                <p>You now have access to the following courses:</p>
                %s
                
                <br/>
                <p>Best regards,<br/>Student Portal Administration</p>
                """.formatted(
                student.getFirstName() + " " + student.getLastName(),
                program.getName(),
                courseHtmlList.toString()
        );

        // 6. Email yuborish
        sendHtmlMail(
                student.getEmail(),
                "Your Program Enrollment Confirmation – " + program.getName(),
                html
        );
    }

    @Override
    public void sendAssignmentCreationMail(AssignmentCreationEvent event) {
        List<Enrollment> enrollmentList = enrollmentRepository.finAllByCourseIdNative(event.getCourseId());
        if (enrollmentList.isEmpty()) {
            throw new IllegalArgumentException("No enrollments found");
        }

        for (Enrollment enrollment : enrollmentList) {
            StudentProfile profile = studentRepository.findByIdAndDeletedAtIsNull(enrollment.getStudent().
                    getId()).orElseThrow(() -> new EntityNotFoundException("Student not found"));

            String html = """
                     <p>Hello <b>%s</b>, You have new assignment pls read carefully</p>
                     <h2>Course is %s</h2>
                    <h2>Deadline is: %s</h2>
                    
                    <p>Best regards,<br/>Student Portal Administration</p>
                    """.formatted(
                    profile.getFirstName() + " " + profile.getLastName(),
                    event.getCourseTitle(),
                    event.getDeadline()
            );
            sendHtmlMail(profile.getEmail(), "New Assignment Creation", html);
        }
    }
}
