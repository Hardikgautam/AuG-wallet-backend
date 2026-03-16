package com.prac_icsd2.dto.response;

import java.time.LocalDate;

import com.prac_icsd2.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPageResponseDTO {

    private Integer customerId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String contactNo;
    private Gender gender;
    private LocalDate registerationDate;
    private String city;
    private String state;
}