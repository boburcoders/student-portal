package com.company.student_portal.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequestDto {
    private String city;
    private String region;
    private String street;
    private int apartmentNumber;
}
