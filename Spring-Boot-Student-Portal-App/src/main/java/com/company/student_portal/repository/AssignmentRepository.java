package com.company.student_portal.repository;

import com.company.student_portal.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByIdAndDeletedAtIsNull(Long assignmentId);

    List<Assignment> findAllByDeletedAtIsNull();

    @Query(
            value = "select * from assignment where course_id=:courseId and deleted_at is null",
            nativeQuery = true
    )
    List<Assignment> findAllByCourseIdNative(@Param("courseId") Long courseId);


    List<Assignment> findAllByDeletedAtIsNullAndDeadline(LocalDateTime targetDay);
}
