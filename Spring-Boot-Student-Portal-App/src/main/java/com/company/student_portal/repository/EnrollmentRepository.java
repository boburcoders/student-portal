package com.company.student_portal.repository;

import com.company.student_portal.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByIdAndDeletedAtIsNull(Long id);

    @Query(
            value = "select * from enrollment as e where e.student_id=:studentId and deleted_at is null",
            nativeQuery = true
    )
    List<Enrollment> findAllByStudentIdNative(@Param("studentId") Long studentId);

    @Query(
            value = "select * from enrollment as e where e.course_id=:courseId and deleted_at is null",
            nativeQuery = true
    )
    List<Enrollment> finAllByCourseIdNative(@Param("courseId") Long courseId);

    @Query(
            value = "select exists(select 1 from enrollment as e where e.course_id=:courseId and e.student_id=:studentId and deleted_at is null)",
            nativeQuery = true
    )
    boolean existsByStudentIdAndCourseIdNative(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Query(
            value = "select e from Enrollment e " +
                    "where e.course.id = ?1 and e.student.id = ?2 and e.deletedAt is null"
    )
    Optional<Enrollment> findEnrollmentByStudentAndCourseId(Long courseId, Long studentId);

}
