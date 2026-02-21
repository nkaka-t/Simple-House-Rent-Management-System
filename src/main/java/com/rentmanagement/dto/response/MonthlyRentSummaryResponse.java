package com.rentmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRentSummaryResponse {
    private Integer month;
    private Integer year;
    private Double totalExpected;
    private Double totalPaid;
    private Double totalUnpaid;
}
