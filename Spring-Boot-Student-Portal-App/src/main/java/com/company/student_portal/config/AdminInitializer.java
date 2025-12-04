package com.company.student_portal.config;

import com.company.student_portal.domain.AuthUser;
import com.company.student_portal.domain.enums.UserRole;
import com.company.student_portal.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final AuthRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Optional<AuthUser> authUser = userRepository.findByUsernameAndDeletedAtIsNull("adminOrg@123");
        if (authUser.isPresent()) {
            log.info("User already exists");
        } else {
            AuthUser newUser = AuthUser
                    .builder()
                    .username("adminOrg@123")
                    .password(passwordEncoder.encode("admin@123"))
                    .isActive(true)
                    .isPasswordReset(true)
                    .role(UserRole.ADMIN)
                    .email("admin@gmail.com")
                    .build();
            userRepository.save(newUser);
            log.info("Admin user created successfully");
        }
    }
}
