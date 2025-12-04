package com.company.student_portal.repository;

import com.company.student_portal.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByIdAndDeletedAtIsNull(Long courseId);

    List<Course> findAllByDeletedAtIsNull();

    @Query(
            value = "select * from course where program_id=:programId and deleted_at is null",
            nativeQuery = true
    )
    List<Course> findAllCourseByProgramIdNative(@Param("programId") Long programId);






}
