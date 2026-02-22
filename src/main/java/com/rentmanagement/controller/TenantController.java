package com.rentmanagement.controller;

import com.rentmanagement.dto.request.TenantRequest;
import com.rentmanagement.dto.response.TenantResponse;
import com.rentmanagement.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(@Valid @RequestBody TenantRequest request) {
        TenantResponse response = tenantService.createTenant(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TenantResponse>> getAllTenants() {
        List<TenantResponse> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<TenantResponse> getTenantById(@PathVariable Long tenantId) {
        TenantResponse response = tenantService.getTenantById(tenantId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{tenantId}")
    public ResponseEntity<TenantResponse> updateTenant(@PathVariable Long tenantId, @RequestBody TenantRequest request) {
        TenantResponse response = tenantService.updateTenant(tenantId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{tenantId}/leave")
    public ResponseEntity<TenantResponse> tenantLeavesHouse(@PathVariable Long tenantId) {
        TenantResponse response = tenantService.tenantLeavesHouse(tenantId);
        return ResponseEntity.ok(response);
    }
}
