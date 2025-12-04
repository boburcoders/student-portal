package com.company.student_portal.repository;

import com.company.student_portal.domain.AuthUser;
import com.company.student_portal.domain.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {
    Optional<University> findByIdAndDeletedAtIsNull(Long id);

    @Query(
            value = "SELECT EXISTS(SELECT 1 FROM university where user_id=:rectorId and deleted_at is null)",
            nativeQuery = true
    )
    boolean existsByRectorNative(@Param("rectorId") Long rectorId);


}
