package com.company.student_portal.service;

import com.company.student_portal.dto.AddressRequestDto;
import com.company.student_portal.dto.HttpApiResponse;
import com.company.student_portal.dto.UniversityRequestDto;
import com.company.student_portal.dto.UniversityResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UniversityService {
    HttpApiResponse<UniversityResponseDto> createUniversity(UniversityRequestDto dto);

    HttpApiResponse<UniversityResponseDto> getUniversityById(Long id);

    HttpApiResponse<UniversityResponseDto> updateUniversity(Long id, UniversityRequestDto dto);

    HttpApiResponse<Boolean> deleteUniversityById(Long id);

    HttpApiResponse<Boolean> addOrUpdateUniversityRector(Long userId, Long id);

    HttpApiResponse<Boolean> addOrUpdateUniversityAddress(AddressRequestDto dto, Long id);
}
