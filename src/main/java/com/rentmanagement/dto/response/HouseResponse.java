package com.rentmanagement.dto.response;

import com.rentmanagement.enums.HouseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseResponse {
    private Long id;
    private String location;
    private Double rentAmount;
    private String description;
    private HouseStatus status;
    private String ownerName;
    private String tenantName;
}
