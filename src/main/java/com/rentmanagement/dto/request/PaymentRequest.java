package com.rentmanagement.dto.request;

import com.rentmanagement.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Month is required")
    private Integer month;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotNull(message = "Amount is required")
    private Double amount;

    private PaymentStatus status;

    private LocalDate paymentDate;

    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotNull(message = "House ID is required")
    private Long houseId;
}