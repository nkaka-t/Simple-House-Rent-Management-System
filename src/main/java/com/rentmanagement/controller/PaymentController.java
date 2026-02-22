package com.rentmanagement.controller;

import com.rentmanagement.dto.request.PaymentRequest;
import com.rentmanagement.dto.response.DebtSummaryResponse;
import com.rentmanagement.dto.response.MonthlyRentSummaryResponse;
import com.rentmanagement.dto.response.PaymentResponse;
import com.rentmanagement.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePayment(@PathVariable Long id, @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate/{tenantId}")
    public ResponseEntity<PaymentResponse> generateMonthlyRent(
            @PathVariable Long tenantId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        PaymentResponse response = paymentService.generateMonthlyRent(tenantId, month, year);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{paymentId}/pay")
    public ResponseEntity<PaymentResponse> markPaymentAsPaid(@PathVariable Long paymentId) {
        PaymentResponse response = paymentService.markPaymentAsPaid(paymentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByTenant(@PathVariable Long tenantId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByTenant(tenantId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/debt/{tenantId}")
    public ResponseEntity<DebtSummaryResponse> getTenantDebt(@PathVariable Long tenantId) {
        DebtSummaryResponse debt = paymentService.calculateTenantDebt(tenantId);
        return ResponseEntity.ok(debt);
    }

    @GetMapping("/debt/total")
    public ResponseEntity<Double> getTotalDebt() {
        Double totalDebt = paymentService.calculateTotalDebt();
        return ResponseEntity.ok(totalDebt);
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlyRentSummaryResponse> getMonthlyRentSummary(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        MonthlyRentSummaryResponse summary = paymentService.monthlyRentSummary(month, year);
        return ResponseEntity.ok(summary);
    }
}
