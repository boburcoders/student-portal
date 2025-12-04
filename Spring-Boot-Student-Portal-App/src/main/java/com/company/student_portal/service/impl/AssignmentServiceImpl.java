package com.company.student_portal.service.impl;

import com.company.student_portal.domain.Assignment;
import com.company.student_portal.domain.Course;
import com.company.student_portal.dto.AssignmentResponseDto;
import com.company.student_portal.dto.AssignmentRequestDto;
import com.company.student_portal.dto.ErrorDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.events.AssignmentCreationEvent;
import com.company.student_portal.repository.AssignmentRepository;
import com.company.student_portal.repository.CourseRepository;
import com.company.student_portal.service.AssignmentService;
import com.company.student_portal.service.mapper.AssignmentMapper;
import com.company.student_portal.service.validation.AssignmentValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final AssignmentMapper assignmentMapper;
    private final AssignmentValidation assignmentValidation;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public HttpApiResponse<AssignmentResponseDto> createAssignment(Long courseId, AssignmentRequestDto dto) {
        Course course = courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        ErrorDto error = assignmentValidation.validateAssignment(dto);
        if (error != null) {
            return HttpApiResponse.<AssignmentResponseDto>builder()
                    .success(false)
                    .message(error.getMessage())
                    .code(error.getCode())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(error)
                    .build();
        }

        Assignment entity = assignmentMapper.toEntity(dto);
        entity.setCourse(course);

        Assignment result = assignmentRepository.save(entity);

        applicationEventPublisher.publishEvent(new AssignmentCreationEvent(this, courseId, result.getTitle(),
                course.getTitle(), result.getDeadline(), "student123"));

        return HttpApiResponse.<AssignmentResponseDto>builder()
                .success(true)
                .message("Assignment created successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(assignmentMapper.toDto(result))
                .build();
    }

    @Override
    public HttpApiResponse<AssignmentResponseDto> getAssignmentById(Long assignmentId) {
        Assignment assignment = assignmentRepository.findByIdAndDeletedAtIsNull(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with id: " + assignmentId));

        return HttpApiResponse.<AssignmentResponseDto>builder()
                .success(true)
                .message("Assignment found")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(assignmentMapper.toDto(assignment))
                .build();
    }

    @Override
    public HttpApiResponse<List<AssignmentResponseDto>> getAllAssignments() {
        List<Assignment> assignmentList = assignmentRepository.findAllByDeletedAtIsNull();
        if (assignmentList.isEmpty()) {
            return HttpApiResponse.<List<AssignmentResponseDto>>builder()
                    .success(false)
                    .message("Assignments not found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return HttpApiResponse.<List<AssignmentResponseDto>>builder()
                .success(true)
                .message("Assignments found")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(assignmentList.stream().map(assignmentMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<List<AssignmentResponseDto>> getAllByCourseId(Long courseId) {
        List<Assignment> assignments = assignmentRepository.findAllByCourseIdNative(courseId);

        if (assignments.isEmpty()) {
            return HttpApiResponse.<List<AssignmentResponseDto>>builder()
                    .success(false)
                    .message("Assignments not found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return HttpApiResponse.<List<AssignmentResponseDto>>builder()
                .success(true)
                .message("Assignments found")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(assignments.stream().map(assignmentMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<AssignmentResponseDto> updateAssignmentById(Long assignmentId, AssignmentRequestDto dto) {
        var assignment = assignmentRepository.findByIdAndDeletedAtIsNull(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with id: " + assignmentId));

        var updatedEntity = assignmentMapper.updateEntity(assignment, dto);
        assignmentRepository.save(updatedEntity);

        return HttpApiResponse.<AssignmentResponseDto>builder()
                .success(true)
                .message("Assignment updated successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(assignmentMapper.toDto(updatedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> deleteAssignmentById(Long assignmentId) {
        Assignment assignment = assignmentRepository.findByIdAndDeletedAtIsNull(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with id: " + assignmentId));

        assignment.setDeletedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("Assignment deleted successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }
}
