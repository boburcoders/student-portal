package com.company.student_portal.service.impl;

import com.company.student_portal.domain.Assignment;
import com.company.student_portal.domain.StudentProfile;
import com.company.student_portal.domain.Submission;
import com.company.student_portal.dto.ErrorDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.SubmissionResponseDto;
import com.company.student_portal.dto.SubmissionUpdateDto;
import com.company.student_portal.events.AssignmentMarkEvent;
import com.company.student_portal.events.StudentSubmissionEvent;
import com.company.student_portal.repository.AssignmentRepository;
import com.company.student_portal.repository.StudentProfileRepository;
import com.company.student_portal.repository.SubmissionRepository;
import com.company.student_portal.service.SubmissionService;
import com.company.student_portal.service.mapper.SubmissionMapper;
import com.company.student_portal.service.validation.SubmissionValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionMapper submissionMapper;
    private final SubmissionValidation submissionValidation;

    private final ApplicationEventPublisher publisher;

    @Override
    public HttpApiResponse<SubmissionResponseDto> createSubmission(Long studentId, Long assignmentId, MultipartFile file) {
        ErrorDto error = submissionValidation.validateSubmission(file);
        if (error != null) {
            return HttpApiResponse.<SubmissionResponseDto>builder()
                    .success(false)
                    .message(error.getMessage())
                    .code(error.getCode())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(error)
                    .build();
        }
        StudentProfile profile = studentProfileRepository.findByIdAndDeletedAtIsNull(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with given id: " + studentId));

        Assignment assignment = assignmentRepository.findByIdAndDeletedAtIsNull(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with given id: " + assignmentId));
        // s3 bilan url olinadi
        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(profile);
        submission.setFileUrl(file.getOriginalFilename());

        Submission savedEntity = submissionRepository.save(submission);

        publisher.publishEvent(new StudentSubmissionEvent(profile.getEmail(),
                assignment.getCourse().getTeacher().getEmail(), assignmentId));

        return HttpApiResponse.<SubmissionResponseDto>builder()
                .success(true)
                .message("Submission created successfully")
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(submissionMapper.toDto(savedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<SubmissionResponseDto> getSubmissionById(Long submissionId) {
        Submission submission = submissionRepository.findByIdAndDeletedAtIsNull(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found with given id: " + submissionId));

        return HttpApiResponse.<SubmissionResponseDto>builder()
                .success(true)
                .message("Submission found")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(submissionMapper.toDto(submission))
                .build();
    }

    @Override
    public HttpApiResponse<List<SubmissionResponseDto>> getSubmissionsByStudentId(Long studentId) {
        studentProfileRepository.findByIdAndDeletedAtIsNull(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with given id: " + studentId));

        List<Submission> submissionList = submissionRepository.findAllByStudentIdNative(studentId);

        if (submissionList.isEmpty()) {
            return HttpApiResponse.<List<SubmissionResponseDto>>builder()
                    .success(false)
                    .message("No submissions found")
                    .code(HttpStatus.NO_CONTENT.value())
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return HttpApiResponse.<List<SubmissionResponseDto>>builder()
                .success(true)
                .message("Submissions found")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(submissionList.stream().map(submissionMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<List<SubmissionResponseDto>> getSubmissionsByAssignmentId(Long assignmentId) {
        assignmentRepository.findByIdAndDeletedAtIsNull(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with given id: " + assignmentId));

        List<Submission> submissionList = submissionRepository.findAllByAssignmentIdNative(assignmentId);

        if (submissionList.isEmpty()) {
            return HttpApiResponse.<List<SubmissionResponseDto>>builder()
                    .success(false)
                    .message("No submissions found")
                    .code(HttpStatus.NO_CONTENT.value())
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return HttpApiResponse.<List<SubmissionResponseDto>>builder()
                .success(true)
                .message("Submissions found")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(submissionList.stream().map(submissionMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<SubmissionResponseDto> updateSubmission(Long submissionId, SubmissionUpdateDto dto) {
        Submission submission = submissionRepository.findByIdAndDeletedAtIsNull(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found with given id: " + submissionId));
        Submission updatedEntity = submissionMapper.updateEntity(submission, dto);
        submissionRepository.save(updatedEntity);

        publisher.publishEvent(new AssignmentMarkEvent(submission.getStudent().getEmail(), submissionId));

        return HttpApiResponse.<SubmissionResponseDto>builder()
                .success(true)
                .message("Submission updated successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(submissionMapper.toDto(updatedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> deleteSubmission(Long submissionId) {
        Submission submission = submissionRepository.findByIdAndDeletedAtIsNull(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found with given id: " + submissionId));

        submission.setDeletedAt(LocalDateTime.now());
        submissionRepository.save(submission);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("Submission deleted successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }
}
