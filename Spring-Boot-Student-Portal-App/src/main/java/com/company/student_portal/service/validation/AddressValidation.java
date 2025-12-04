package com.company.student_portal.service.validation;

import com.company.student_portal.dto.AddressRequestDto;
import com.company.student_portal.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AddressValidation {
    public ErrorDto validateAddress(AddressRequestDto dto) {
        if (dto.getCity() == null) {
            return new ErrorDto("/address", "Address city must not be null", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getStreet() == null) {
            return new ErrorDto("/address", "Address street must not be null", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getRegion() == null) {
            return new ErrorDto("/address", "Address region must not be null", HttpStatus.BAD_REQUEST.value());
        }
        if (dto.getApartmentNumber() < 0) {
            return new ErrorDto("/address", "Apartment number must not be negative", HttpStatus.BAD_REQUEST.value());
        }
        return null;
    }
}
