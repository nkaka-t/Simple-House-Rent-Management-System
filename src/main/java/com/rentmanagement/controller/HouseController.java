package com.rentmanagement.controller;

import com.rentmanagement.dto.request.HouseRequest;
import com.rentmanagement.dto.response.HouseResponse;
import com.rentmanagement.service.HouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @PostMapping
    public ResponseEntity<HouseResponse> createHouse(@Valid @RequestBody HouseRequest request) {
        HouseResponse response = houseService.createHouse(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HouseResponse>> getAllHouses() {
        List<HouseResponse> houses = houseService.getAllHouses();
        return ResponseEntity.ok(houses);
    }

    @PutMapping("/{houseId}/assign/{tenantId}")
    public ResponseEntity<HouseResponse> assignTenantToHouse(
            @PathVariable Long houseId,
            @PathVariable Long tenantId) {
        HouseResponse response = houseService.assignTenantToHouse(houseId, tenantId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{houseId}/vacant")
    public ResponseEntity<HouseResponse> markHouseVacant(@PathVariable Long houseId) {
        HouseResponse response = houseService.markHouseVacant(houseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{houseId}")
    public ResponseEntity<HouseResponse> getHouseById(@PathVariable Long houseId) {
        HouseResponse response = houseService.getHouseById(houseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vacant")
    public ResponseEntity<List<HouseResponse>> getVacantHouses() {
        List<HouseResponse> vacantHouses = houseService.getVacantHouses();
        return ResponseEntity.ok(vacantHouses);
    }
}
