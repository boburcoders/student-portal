package com.company.student_portal.service.impl;

import com.company.student_portal.domain.Course;
import com.company.student_portal.domain.Enrollment;
import com.company.student_portal.domain.StudentProfile;
import com.company.student_portal.domain.enums.EnrollmentStatus;
import com.company.student_portal.dto.EnrollmentResponseDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.events.ProgramEnrollmentCreatedEvent;
import com.company.student_portal.repository.CourseRepository;
import com.company.student_portal.repository.EnrollmentRepository;
import com.company.student_portal.repository.StudentProfileRepository;
import com.company.student_portal.service.EnrollmentService;
import com.company.student_portal.service.mapper.EnrollmentMapper;
import com.company.student_portal.service.validation.EnrollmentValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final EnrollmentValidation enrollmentValidation;


    @Override
    public HttpApiResponse<EnrollmentResponseDto> createEnrollment(Long studentId, Long courseId) {
        boolean b = enrollmentRepository.existsByStudentIdAndCourseIdNative(studentId, courseId);
        if (b) {
            throw new IllegalArgumentException("Student already enrolled for this course");
        }

        Course course = courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        StudentProfile profile = studentProfileRepository.findByIdAndDeletedAtIsNull(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(profile);
        enrollment.setStatus(EnrollmentStatus.PENDING);

        Enrollment savedEntity = enrollmentRepository.save(enrollment);


        return HttpApiResponse.<EnrollmentResponseDto>builder()
                .success(true)
                .message("Enrollment created successfully")
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(enrollmentMapper.toDto(savedEntity))
                .build();
    }


    @Override
    @Transactional
    public void createEnrollmentByProgramId(Long studentId, Long programId) {
        StudentProfile profile = studentProfileRepository.findByIdAndDeletedAtIsNull(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));

        List<Course> courseList = courseRepository.findAllCourseByProgramIdNative(programId);

        for (Course course : courseList) {
            Enrollment enrollment = new Enrollment();
            enrollment.setCourse(course);
            enrollment.setStudent(profile);
            enrollment.setStatus(EnrollmentStatus.PENDING);
            enrollmentRepository.save(enrollment);
        }
    }

    @Override
    @Transactional
    public void confirmCourseEnrollmentByProgramId(Long studentId, Long programId) {

        // 1. Programdagi barcha kurslar
        List<Course> courses = courseRepository.findAllCourseByProgramIdNative(programId);

        for (Course course : courses) {
            System.out.println(course.getId());
            Enrollment enrollment = enrollmentRepository
                    .findEnrollmentByStudentAndCourseId(course.getId(),studentId)
                    .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));

            enrollment.setStatus(EnrollmentStatus.ENROLLED);

            enrollmentRepository.save(enrollment);
        }
    }


    @Override
    public HttpApiResponse<EnrollmentResponseDto> getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found with id: " + id));

        return HttpApiResponse.<EnrollmentResponseDto>builder()
                .success(true)
                .message("Enrollment found successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(enrollmentMapper.toDto(enrollment))
                .build();
    }

    @Override
    public HttpApiResponse<List<EnrollmentResponseDto>> getAllByStudentId(Long studentId) {
        List<Enrollment> enrollmentList = enrollmentRepository.findAllByStudentIdNative(studentId);
        if (enrollmentList.isEmpty()) {
            return HttpApiResponse.<List<EnrollmentResponseDto>>builder()
                    .success(false)
                    .message("No enrollments found")
                    .code(HttpStatus.NO_CONTENT.value())
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return HttpApiResponse.<List<EnrollmentResponseDto>>builder()
                .success(true)
                .message("All enrollments found successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(enrollmentList.stream().map(enrollmentMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<List<EnrollmentResponseDto>> getAllByCourseId(Long courseId) {

        List<Enrollment> enrollmentList = enrollmentRepository.finAllByCourseIdNative(courseId);

        if (enrollmentList.isEmpty()) {
            return HttpApiResponse.<List<EnrollmentResponseDto>>builder()
                    .success(false)
                    .message("No enrollments found")
                    .code(HttpStatus.NO_CONTENT.value())
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return HttpApiResponse.<List<EnrollmentResponseDto>>builder()
                .success(true)
                .message("All enrollments found successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(enrollmentList.stream().map(enrollmentMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> confirmEnrollment(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findEnrollmentByStudentAndCourseId(studentId, courseId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Enrollment not found with studentId: %d and courseId: %d", studentId, courseId)));

        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        enrollmentRepository.save(enrollment);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("Enrollment confirmed successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> deleteEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found with id: " + id));

        enrollment.setDeletedAt(LocalDateTime.now());
        enrollmentRepository.save(enrollment);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("Enrollment deleted successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }
}
