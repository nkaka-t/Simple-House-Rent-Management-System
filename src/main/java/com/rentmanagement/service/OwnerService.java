package com.rentmanagement.service;

import com.rentmanagement.dto.request.OwnerRequest;
import com.rentmanagement.dto.response.OwnerResponse;
import com.rentmanagement.entity.Owner;
import com.rentmanagement.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Transactional
    public OwnerResponse createOwner(OwnerRequest request) {
        Owner owner = new Owner();
        owner.setName(request.getName());
        owner.setPhone(request.getPhone());
        owner.setEmail(request.getEmail());

        Owner savedOwner = ownerRepository.save(owner);
        return convertToResponse(savedOwner);
    }

    public List<OwnerResponse> getAllOwners() {
        return ownerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private OwnerResponse convertToResponse(Owner owner) {
        return new OwnerResponse(
                owner.getId(),
                owner.getName(),
                owner.getPhone(),
                owner.getEmail()
        );
    }
}
