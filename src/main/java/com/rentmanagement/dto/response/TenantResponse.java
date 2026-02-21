package com.rentmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantResponse {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String nationalId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String houseLocation;
}
