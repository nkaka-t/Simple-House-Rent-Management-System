package com.rentmanagement.service;

import com.rentmanagement.dto.request.TenantRequest;
import com.rentmanagement.dto.response.TenantResponse;
import com.rentmanagement.entity.House;
import com.rentmanagement.entity.Tenant;
import com.rentmanagement.enums.HouseStatus;
import com.rentmanagement.exception.ResourceNotFoundException;
import com.rentmanagement.repository.HouseRepository;
import com.rentmanagement.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final HouseRepository houseRepository;

    @Transactional
    public TenantResponse createTenant(TenantRequest request) {
        Tenant tenant = new Tenant();
        tenant.setFullName(request.getFullName());
        tenant.setPhone(request.getPhone());
        tenant.setEmail(request.getEmail());
        tenant.setNationalId(request.getNationalId());
        tenant.setStartDate(request.getStartDate());
        tenant.setEndDate(request.getEndDate());

        Tenant savedTenant = tenantRepository.save(tenant);
        return convertToResponse(savedTenant);
    }

    public List<TenantResponse> getAllTenants() {
        return tenantRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TenantResponse tenantLeavesHouse(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + tenantId));

        if (tenant.getHouse() != null) {
            House house = tenant.getHouse();
            house.setStatus(HouseStatus.VACANT);
            house.setTenant(null);
            houseRepository.save(house);

            tenant.setHouse(null);
        }

        Tenant savedTenant = tenantRepository.save(tenant);
        return convertToResponse(savedTenant);
    }

    private TenantResponse convertToResponse(Tenant tenant) {
        return new TenantResponse(
                tenant.getId(),
                tenant.getFullName(),
                tenant.getPhone(),
                tenant.getEmail(),
                tenant.getNationalId(),
                tenant.getStartDate(),
                tenant.getEndDate(),
                tenant.getHouse() != null ? tenant.getHouse().getLocation() : null
        );
    }
}
