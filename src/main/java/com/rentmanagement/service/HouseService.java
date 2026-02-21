package com.rentmanagement.service;

import com.rentmanagement.dto.request.HouseRequest;
import com.rentmanagement.dto.response.HouseResponse;
import com.rentmanagement.entity.House;
import com.rentmanagement.entity.Owner;
import com.rentmanagement.entity.Tenant;
import com.rentmanagement.enums.HouseStatus;
import com.rentmanagement.exception.BusinessLogicException;
import com.rentmanagement.exception.ResourceNotFoundException;
import com.rentmanagement.repository.HouseRepository;
import com.rentmanagement.repository.OwnerRepository;
import com.rentmanagement.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;
    private final OwnerRepository ownerRepository;
    private final TenantRepository tenantRepository;

    @Transactional
    public HouseResponse createHouse(HouseRequest request) {
        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + request.getOwnerId()));

        House house = new House();
        house.setLocation(request.getLocation());
        house.setRentAmount(request.getRentAmount());
        house.setDescription(request.getDescription());
        house.setStatus(HouseStatus.VACANT);
        house.setOwner(owner);

        House savedHouse = houseRepository.save(house);
        return convertToResponse(savedHouse);
    }

    public List<HouseResponse> getAllHouses() {
        return houseRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public HouseResponse assignTenantToHouse(Long houseId, Long tenantId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id: " + houseId));

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + tenantId));

        if (house.getStatus() == HouseStatus.OCCUPIED) {
            throw new BusinessLogicException("House is already occupied");
        }

        if (tenant.getHouse() != null) {
            throw new BusinessLogicException("Tenant is already assigned to another house");
        }

        house.setStatus(HouseStatus.OCCUPIED);
        house.setTenant(tenant);
        tenant.setHouse(house);

        houseRepository.save(house);
        tenantRepository.save(tenant);

        return convertToResponse(house);
    }

    @Transactional
    public HouseResponse markHouseVacant(Long houseId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id: " + houseId));

        if (house.getTenant() != null) {
            Tenant tenant = house.getTenant();
            tenant.setHouse(null);
            tenantRepository.save(tenant);
        }

        house.setStatus(HouseStatus.VACANT);
        house.setTenant(null);

        House savedHouse = houseRepository.save(house);
        return convertToResponse(savedHouse);
    }

    public List<HouseResponse> getVacantHouses() {
        return houseRepository.findByStatus(HouseStatus.VACANT)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private HouseResponse convertToResponse(House house) {
        return new HouseResponse(
                house.getId(),
                house.getLocation(),
                house.getRentAmount(),
                house.getDescription(),
                house.getStatus(),
                house.getOwner().getName(),
                house.getTenant() != null ? house.getTenant().getFullName() : null
        );
    }
}
