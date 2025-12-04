package com.company.student_portal.service.impl;

import com.company.student_portal.config.jwt.JwtService;
import com.company.student_portal.domain.AuthUser;
import com.company.student_portal.domain.StudentProfile;
import com.company.student_portal.domain.TeacherProfile;
import com.company.student_portal.domain.enums.UserRole;
import com.company.student_portal.dto.*;
import com.company.student_portal.repository.AuthRepository;
import com.company.student_portal.service.UserService;
import com.company.student_portal.service.mapper.AuthMapper;
import com.company.student_portal.service.validation.AuthValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthRepository authUserRepository;
    private final AuthMapper authMapper;
    private final AuthValidation authValidation;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public HttpApiResponse<AuthResponseDto> getCurrentUser(Authentication authentication) {
        AuthUser authUser = authUserRepository.findByUsernameAndDeletedAtIsNull(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found with name " + authentication.getName()));

        return HttpApiResponse.<AuthResponseDto>builder()
                .success(true)
                .message("OK")
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(authMapper.toDto(authUser))
                .build();
    }

    @Override
    public HttpApiResponse<TokenResponse> getAccessToken(TokenRequest request) {

        AuthUser authUser = authUserRepository
                .findByUsernameAndDeletedAtIsNull(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with given username: " + request.getUsername()
                ));

        if (!passwordEncoder.matches(request.getPassword(), authUser.getPassword())) {
            throw new IllegalArgumentException("Password is not matching");
        }

        Map<String, Object> claims = Map.of(
                "userId", authUser.getId(),
                "role", authUser.getRole().name()
        );

        TokenResponse tokens = new TokenResponse(
                jwtService.generateAccessToken(authUser.getUsername(), claims),
                jwtService.generateRefreshToken(authUser.getUsername(), claims)
        );

        return HttpApiResponse.<TokenResponse>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(tokens)
                .build();
    }


    @Override
    public HttpApiResponse<TokenResponse> refreshAccessToken(String refreshToken) {

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);

        AuthUser user = authUserRepository
                .findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user.getUsername())) {
            throw new IllegalArgumentException("Refresh token is not valid or expired");
        }

        Map<String, Object> claims = Map.of(
                "userId", user.getId(),
                "role", user.getRole().name()
        );

        TokenResponse response = new TokenResponse(
                jwtService.generateAccessToken(username, claims),
                refreshToken  // eski refreshni qaytaramiz (best practice)
        );

        return HttpApiResponse.<TokenResponse>builder()
                .success(true)
                .message("OK")
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }


    @Override
    @Transactional
    public HttpApiResponse<AuthResponseDto> registerUser(AuthCreateRequestDto dto) {
        ErrorDto error = authValidation.validateUser(dto);
        if (error != null) {
            return HttpApiResponse.<AuthResponseDto>builder()
                    .success(false)
                    .message(error.getMessage())
                    .code(error.getCode())
                    .status(HttpStatus.BAD_REQUEST)
                    .error(error)
                    .build();
        }

        AuthUser user;

        switch (dto.getRole()) {
            case STUDENT -> {
                var studentProfile = authMapper.mapToStudentProfile(dto);

                studentProfile.setPassword(passwordEncoder.encode(dto.getPassword()));//hashlangan boladi
                studentProfile.setIsActive(true);
                studentProfile.setStudentNumber(dto.getUsername());
                studentProfile.setIsPasswordReset(false);

                user = studentProfile;

            }
            case TEACHER -> {
                var teacherProfile = authMapper.mapToTeacherProfile(dto);

                teacherProfile.setPassword(passwordEncoder.encode(dto.getPassword())); // hash boladi
                teacherProfile.setIsActive(true);
                teacherProfile.setIsPasswordReset(false);

                user = teacherProfile;

            }
            case ADMIN -> {
                var admin = authMapper.toEntity(dto);
                admin.setPassword(passwordEncoder.encode(dto.getPassword())); // hash boladi
                admin.setIsActive(true);
                admin.setIsPasswordReset(false);
                user = admin;
            }
            default -> {
                return HttpApiResponse.<AuthResponseDto>builder()
                        .success(false)
                        .message("Invalid user role")
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build();
            }
        }

        authUserRepository.save(user);

        return HttpApiResponse.<AuthResponseDto>builder()
                .success(true)
                .message("User registered successfully")
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(authMapper.toDto(user))
                .build();
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> registerStudentByFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        List<StudentProfile> studentProfiles = new ArrayList<>();
        List<String> headers = List.of("username", "email", "password", "firstName", "lastName", "phoneNumber");

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreEmptyLines(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {
            List<String> headerNames = parser.getHeaderNames();
            for (String header : headerNames) {
                if (!headers.contains(header)) {
                    throw new IllegalArgumentException("Header " + header + " not found");
                }
            }
            for (CSVRecord record : parser) {
                try {
                    String username = record.get("username");
                    String password = record.get("password");
                    String email = record.get("email");
                    String firstName = record.get("firstName");
                    String lastName = record.get("lastName");
                    String phoneNumber = record.get("phoneNumber");

                    AuthCreateRequestDto dto = new AuthCreateRequestDto();
                    dto.setUsername(username.trim());
                    dto.setEmail(email.trim());
                    dto.setPassword(password.trim());
                    dto.setRole(UserRole.STUDENT);

                    ErrorDto errorDto = authValidation.validateUser(dto);
                    if (errorDto != null) {
                        return HttpApiResponse.<Boolean>builder()
                                .success(false)
                                .message(errorDto.getMessage())
                                .code(errorDto.getCode())
                                .status(HttpStatus.BAD_REQUEST)
                                .error(errorDto)
                                .build();
                    }
                    var entity = authMapper.mapToStudentProfile(dto);
                    entity.setFirstName(firstName.trim());
                    entity.setLastName(lastName.trim());
                    entity.setPhoneNumber(phoneNumber.trim());
                    entity.setStudentNumber(username.trim());
                    entity.setPassword(password);  //hash boladi
                    entity.setIsActive(true);
                    entity.setIsPasswordReset(false);

                    studentProfiles.add(entity);

                    successCount++;
                } catch (IllegalArgumentException e) {
                    errors.add("Row " + record.getRecordNumber() + ": Invalid column - " + e.getMessage());
                    failCount++;
                } catch (Exception e) {
                    errors.add("Row " + record.getRecordNumber() + ": " + e.getMessage());
                    failCount++;
                }
            }

            if (successCount == 0) {
                throw new IllegalArgumentException("No students registered. Errors: " + String.join("; ", errors));
            }

            String message = String.format("Successfully registered %d students. Failed: %d", successCount, failCount);
            if (!errors.isEmpty()) {
                message += ". Errors: " + String.join("; ", errors);
            }

            authUserRepository.saveAll(studentProfiles);

            return HttpApiResponse.<Boolean>builder()
                    .success(true)
                    .message(message)
                    .status(HttpStatus.CREATED)
                    .code(HttpStatus.CREATED.value())
                    .data(true)
                    .build();

        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read file: " + e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public HttpApiResponse<Boolean> registerInstructorByFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;
        List<TeacherProfile> teacherProfiles = new ArrayList<>();

        List<String> headers = List.of("username", "email", "password", "firstName", "lastName", "phoneNumber");

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreEmptyLines(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {
            List<String> headerNames = parser.getHeaderNames();
            for (String header : headerNames) {
                if (!headers.contains(header)) {
                    throw new IllegalArgumentException("Header " + header + " not found");
                }
            }
            for (CSVRecord record : parser) {
                try {
                    String username = record.get("username");
                    String password = record.get("password");
                    String email = record.get("email");
                    String firstName = record.get("firstName");
                    String lastName = record.get("lastName");
                    String phoneNumber = record.get("phoneNumber");

                    AuthCreateRequestDto dto = new AuthCreateRequestDto();
                    dto.setUsername(username.trim());
                    dto.setEmail(email.trim());
                    dto.setPassword(password.trim());
                    dto.setRole(UserRole.TEACHER);

                    ErrorDto errorDto = authValidation.validateUser(dto);
                    if (errorDto != null) {
                        return HttpApiResponse.<Boolean>builder()
                                .success(false)
                                .message(errorDto.getMessage())
                                .code(errorDto.getCode())
                                .status(HttpStatus.BAD_REQUEST)
                                .error(errorDto)
                                .build();
                    }
                    var entity = authMapper.mapToTeacherProfile(dto);
                    entity.setFirstName(firstName.trim());
                    entity.setLastName(lastName.trim());
                    entity.setPhoneNumber(phoneNumber.trim());
                    entity.setPassword(password); // hash boladi
                    entity.setIsActive(true);
                    entity.setIsPasswordReset(false);

                    teacherProfiles.add(entity);

                    successCount++;
                } catch (IllegalArgumentException e) {
                    errors.add("Row " + record.getRecordNumber() + ": Invalid column - " + e.getMessage());
                    failCount++;
                } catch (Exception e) {
                    errors.add("Row " + record.getRecordNumber() + ": " + e.getMessage());
                    failCount++;
                }
            }

            if (successCount == 0) {
                throw new IllegalArgumentException("No instructor registered. Errors: " + String.join("; ", errors));
            }

            String message = String.format("Successfully registered %d instructor. Failed: %d", successCount, failCount);
            if (!errors.isEmpty()) {
                message += ". Errors: " + String.join("; ", errors);
            }
            authUserRepository.saveAll(teacherProfiles);
            return HttpApiResponse.<Boolean>builder()
                    .success(true)
                    .message(message)
                    .status(HttpStatus.CREATED)
                    .code(HttpStatus.CREATED.value())
                    .data(true)
                    .build();

        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read file: " + e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected error: " + e.getMessage());
        }
    }
}
