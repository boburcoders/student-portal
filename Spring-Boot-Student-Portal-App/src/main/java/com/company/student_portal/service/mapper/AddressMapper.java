package com.company.student_portal.service.mapper;

import com.company.student_portal.domain.Address;
import com.company.student_portal.dto.AddressRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressRequestDto dto);
}
