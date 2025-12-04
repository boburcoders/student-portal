package com.company.student_portal.repository;

import com.company.student_portal.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    boolean existsByNameAndDeletedAtIsNull(String name);

    Optional<Program> findByIdAndDeletedAtIsNull(Long programId);

    @Query(value = "select * from program where department_id=:departmentId and deleted_at is null ",
            nativeQuery = true)
    List<Program> findAllByDepartmentIdAndDeletedAtIsNullWithNative(@Param("departmentId") Long departmentId);
}
