package com.company.student_portal.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EmailDto {
    private String to;
    private String subject;
    private String body;
}
