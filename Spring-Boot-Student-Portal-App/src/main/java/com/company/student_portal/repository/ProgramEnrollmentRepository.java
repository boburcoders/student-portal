package com.company.student_portal.repository;

import com.company.student_portal.domain.ProgramEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramEnrollmentRepository extends JpaRepository<ProgramEnrollment, Long> {
    Optional<ProgramEnrollment> findByIdAndDeletedAtIsNull(Long id);

    @Query(
            value = "select exists(select 1 from program_enrollment  e where e.student_id=:studentId and e.program_id=:programId and e.deleted_at is null)",
            nativeQuery = true
    )
    boolean existsByStudentIdAndProgramIdAndDeletedAtIsNull(@Param("studentId") Long studentId, @Param("programId") Long programId);

    @Query("select t from ProgramEnrollment t where t.student.id=?1 and t.deletedAt is null")
    ProgramEnrollment findByStudentIdWithNative(Long studentId);

    List<ProgramEnrollment> findAllByProgramIdAndDeletedAtIsNull(Long programId);
}
