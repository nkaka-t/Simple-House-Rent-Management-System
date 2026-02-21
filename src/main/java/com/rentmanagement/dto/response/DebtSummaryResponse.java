package com.rentmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtSummaryResponse {
    private Long tenantId;
    private String tenantName;
    private Double totalDebt;
}
