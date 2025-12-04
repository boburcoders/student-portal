package com.company.student_portal.service.impl;

import com.company.student_portal.domain.Course;
import com.company.student_portal.domain.Program;
import com.company.student_portal.domain.TeacherProfile;
import com.company.student_portal.dto.CourseRequestDto;
import com.company.student_portal.dto.CourseResponseDto;
import com.company.student_portal.dto.ErrorDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.repository.CourseRepository;
import com.company.student_portal.repository.ProgramRepository;
import com.company.student_portal.repository.TeacherProfileRepository;
import com.company.student_portal.service.CourseService;
import com.company.student_portal.service.mapper.CourseMapper;
import com.company.student_portal.service.validation.CourseValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final ProgramRepository programRepository;
    private final CourseMapper courseMapper;
    private final CourseValidation courseValidation;


    @Override
    public HttpApiResponse<CourseResponseDto> createCourse(Long programId, CourseRequestDto dto) {
        ErrorDto error = courseValidation.validateCourse(dto);
        if (error != null) {
            return HttpApiResponse.<CourseResponseDto>builder()
                    .success(false)
                    .message(error.getMessage())
                    .code(error.getCode())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(error)
                    .build();
        }
        Program program = programRepository.findByIdAndDeletedAtIsNull(programId)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id " + programId));

        Course entity = courseMapper.toEntity(dto);
        entity.setProgram(program);

        Course newEntity = courseRepository.save(entity);

        return HttpApiResponse.<CourseResponseDto>builder()
                .success(true)
                .message("Course created successfully")
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(courseMapper.toDto(newEntity))
                .build();
    }

    @Override
    public HttpApiResponse<CourseResponseDto> getCourseById(Long courseId) {
        Course course = courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + courseId));

        return HttpApiResponse.<CourseResponseDto>builder()
                .success(true)
                .message("Course found successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(courseMapper.toDto(course))
                .build();
    }

    @Override
    public HttpApiResponse<List<CourseResponseDto>> getAllCourse() {
        List<Course> courseList = courseRepository.findAllByDeletedAtIsNull();
        if (courseList.isEmpty()) {
            return HttpApiResponse.<List<CourseResponseDto>>builder()
                    .success(false)
                    .message("Course not found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return HttpApiResponse.<List<CourseResponseDto>>builder()
                .success(true)
                .message("Course found successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(courseList.stream().map(courseMapper::toDto).toList())
                .build();
    }

    @Override
    public HttpApiResponse<List<CourseResponseDto>> getAllCourseByProgramId(Long programId) {
        programRepository.findByIdAndDeletedAtIsNull(programId)
                .orElseThrow(() -> new EntityNotFoundException("Program not found with id " + programId));
        List<Course> courseList = courseRepository.findAllCourseByProgramIdNative(programId);
        if (courseList.isEmpty()) {
            return HttpApiResponse.<List<CourseResponseDto>>builder()
                    .success(false)
                    .message("Course not found")
                    .code(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return HttpApiResponse.<List<CourseResponseDto>>builder()
                .success(true)
                .message("Course found successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(courseList.stream().map(courseMapper::toDto).toList())
                .build();
    }


    @Override
    public HttpApiResponse<Boolean> addTeacherToCourse(Long teacherId, Long courseId) {
        Course course = courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + courseId));

        TeacherProfile teacherProfile = teacherProfileRepository.findByIdAndDeletedAtIsNull(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id " + teacherId));

        if (course.getTeacher() != null) {
            throw new IllegalArgumentException("Teacher is already in this Course");
        }
        course.setTeacher(teacherProfile);
        courseRepository.save(course);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("Teacher added successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> removeTeacherFromCourse(Long teacherId, Long courseId) {
        Course course = courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + courseId));

        teacherProfileRepository.findByIdAndDeletedAtIsNull(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id " + teacherId));

        if (Objects.equals(course.getTeacher().getId(), teacherId)) {
            course.setTeacher(null);
            courseRepository.save(course);
            return HttpApiResponse.<Boolean>builder()
                    .success(true)
                    .message("Teacher removed successfully")
                    .code(HttpStatus.OK.value())
                    .status(HttpStatus.OK)
                    .data(true)
                    .build();
        }
        return HttpApiResponse.<Boolean>builder()
                .success(false)
                .message("Teacher is not in this Course")
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND)
                .data(false)
                .build();
    }

    @Override
    public HttpApiResponse<CourseResponseDto> updateCourseById(Long id, CourseRequestDto dto) {
        Course course = courseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + id));

        Course updatedCourse = courseMapper.updateCourse(course, dto);

        courseRepository.save(updatedCourse);

        return HttpApiResponse.<CourseResponseDto>builder()
                .success(true)
                .message("Course updated successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(courseMapper.toDto(updatedCourse))
                .build();
    }

    @Override
    public HttpApiResponse<Boolean> deleteCourseById(Long id) {
        Course course = courseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + id));
        course.setDeletedAt(LocalDateTime.now());
        courseRepository.save(course);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .message("Course deleted successfully")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(true)
                .build();
    }
}
