package com.company.student_portal.repository;

import com.company.student_portal.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findByIdAndIsActiveTrueAndDeletedAtIsNull(Long userId);


    Optional<AuthUser> findByEmailAndDeletedAtIsNull(String email);

    Optional<AuthUser> findByUsernameAndDeletedAtIsNull(String username);


}
