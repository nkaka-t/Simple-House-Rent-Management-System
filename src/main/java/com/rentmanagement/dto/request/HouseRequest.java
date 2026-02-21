package com.rentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseRequest {

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Rent amount is required")
    private Double rentAmount;

    private String description;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;
}
