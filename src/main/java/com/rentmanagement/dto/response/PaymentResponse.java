package com.rentmanagement.dto.response;

import com.rentmanagement.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Integer month;
    private Integer year;
    private Double amount;
    private PaymentStatus status;
    private LocalDate paymentDate;
    private String tenantName;
    private String houseLocation;
}
