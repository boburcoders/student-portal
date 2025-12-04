package com.company.student_portal.repository;

import com.company.student_portal.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findByIdAndDeletedAtIsNull(Long submissionId);

    @Query(
            value = "select * from submission as s where s.student_id=:studentId and deleted_at is null",
            nativeQuery = true
    )
    List<Submission> findAllByStudentIdNative(@Param("studentId") Long studentId);

    @Query(
            value = "select * from submission as s where s.assignment_id=:assignmentId and deleted_at is null",
            nativeQuery = true
    )
    List<Submission> findAllByAssignmentIdNative(@Param("assignmentId") Long assignmentId);

}
