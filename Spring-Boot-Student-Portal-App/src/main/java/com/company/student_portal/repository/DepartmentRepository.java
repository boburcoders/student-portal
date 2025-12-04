package com.company.student_portal.repository;

import com.company.student_portal.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByIdAndDeletedAtIsNull(Long id);

    List<Department> findAllByDeletedAtIsNull();

    @Query(
            value = "select exists (select 1 from department where head_id=:headId and deleted_at is null)",
            nativeQuery = true
    )
    boolean existByHeadUserNative(@Param("headId") Long headId);

    @Query(
            value = "select * from department where university_id=:universityId and deleted_at is null",
            nativeQuery = true
    )
    List<Department> findAllByUniversityIdNative(@Param("universityId") Long universityId);


}
