package com.company.student_portal.service.impl;

import com.company.student_portal.domain.Program;
import com.company.student_portal.domain.ProgramEnrollment;
import com.company.student_portal.domain.StudentProfile;
import com.company.student_portal.domain.enums.EnrollmentStatus;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.ProgramEnrollmentResponseDto;
import com.company.student_portal.events.ProgramEnrollmentConfirmedEvent;
import com.company.student_portal.events.ProgramEnrollmentCreatedEvent;
import com.company.student_portal.repository.ProgramEnrollmentRepository;
import com.company.student_portal.repository.ProgramRepository;
import com.company.student_portal.repository.StudentProfileRepository;
import com.company.student_portal.service.ProgramEnrollmentService;
import com.company.student_portal.service.mapper.ProgramEnrollmentMapper;
import com.company.student_portal.service.validation.ProgramEnrollmentValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProgramEnrollmentServiceImpl implements ProgramEnrollmentService {
    private final ProgramEnrollmentRepository programEnrollmentRepository;
    private final ProgramRepository programRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final ProgramEnrollmentMapper programEnrollmentMapper;
    private final ProgramEnrollmentValidation programEnrollmentValidation;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public HttpApiResponse<ProgramEnrollmentResponseDto> createEnrollment(Long studentId, Long programId) {
        boolean b = programEnrollmentRepository.existsByStudentIdAndProgramIdAndDeletedAtIsNull(studentId, programId);
        if (b) {
            throw new IllegalArgumentException("Student already in Program Enrollment");
        }

        ProgramEnrollment enrollment = programEnrollmentRepository.findByStudentIdWithNative(studentId);
        if (enrollment != null) {
            throw new IllegalArgumentException("Student already in Program Enrollment");
        }

        StudentProfile profile = studentProfileRepository.findByIdAndDeletedAtIsNull(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with given id: " + studentId));

        Program program = programRepository.findByIdAndDeletedAtIsNull(programId)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with given id: " + programId));

        ProgramEnrollment programEnrollment = new ProgramEnrollment();
        programEnrollment.setProgram(program);
        programEnrollment.setStudent(profile);
        programEnrollment.setStatus(EnrollmentStatus.PENDING);

        ProgramEnrollment saved = programEnrollmentRepository.save(programEnrollment);

        applicationEventPublisher.publishEvent(new ProgramEnrollmentCreatedEvent(
                profile.getEmail(), studentId, program.getName(), program.getId()));

        return HttpApiResponse.<ProgramEnrollmentResponseDto>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(programEnrollmentMapper.toDto(saved))
                .build();
    }

    @Override
    public HttpApiResponse<ProgramEnrollmentResponseDto> getEnrollmentById(Long id) {
        ProgramEnrollment enrollment = programEnrollmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with given id: " + id));

        return HttpApiResponse.<ProgramEnrollmentResponseDto>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(programEnrollmentMapper.toDto(enrollment))
                .build();
    }

    @Override
    public HttpApiResponse<ProgramEnrollmentResponseDto> getByStudentId(Long studentId) {
        StudentProfile profile = studentProfileRepository.findByIdAndDeletedAtIsNull(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with given id: " + studentId));

        ProgramEnrollment enrollment = programEnrollmentRepository.findByStudentIdWithNative(studentId);

        if (enrollment == null) {
            throw new IllegalArgumentException("Student already in Program Enrollment");
        }

        return HttpApiResponse.<ProgramEnrollmentResponseDto>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(programEnrollmentMapper.toDto(enrollment))
                .build();
    }

    @Override
    public HttpApiResponse<List<ProgramEnrollmentResponseDto>> getAllByProgramId(Long programId) {
        List<ProgramEnrollment> enrollmentList = programEnrollmentRepository.findAllByProgramIdAndDeletedAtIsNull(programId);
        if (enrollmentList.isEmpty()) {
            return HttpApiResponse.<List<ProgramEnrollmentResponseDto>>builder()
                    .success(false)
                    .message("No enrollments found")
                    .code(HttpStatus.NO_CONTENT.value())
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }


        return HttpApiResponse.<List<ProgramEnrollmentResponseDto>>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(enrollmentList.stream().map(programEnrollmentMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> confirmEnrollment(Long studentId, Long programEnrollmentId) {
        ProgramEnrollment programEnrollment = programEnrollmentRepository.findByIdAndDeletedAtIsNull(programEnrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Program Enrollment not found with given id: " + programEnrollmentId));

        if (programEnrollment.getStatus().equals(EnrollmentStatus.ENROLLED)) {
            throw new IllegalArgumentException("Program Enrollment is already enrolled");
        }
        programEnrollment.setStatus(EnrollmentStatus.ENROLLED);
        programEnrollmentRepository.save(programEnrollment);

        applicationEventPublisher.publishEvent(new ProgramEnrollmentConfirmedEvent(studentId,
                programEnrollmentId, programEnrollment.getProgram().getId()));

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> deleteEnrollmentById(Long id) {
        ProgramEnrollment programEnrollment = programEnrollmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with given id: " + id));
        programEnrollment.setDeletedAt(LocalDateTime.now());
        programEnrollmentRepository.save(programEnrollment);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }
}
