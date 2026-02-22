package com.rentmanagement.service;

import com.rentmanagement.dto.response.DebtSummaryResponse;
import com.rentmanagement.dto.response.MonthlyRentSummaryResponse;
import com.rentmanagement.dto.response.PaymentResponse;
import com.rentmanagement.entity.House;
import com.rentmanagement.entity.Payment;
import com.rentmanagement.entity.Tenant;
import com.rentmanagement.enums.PaymentStatus;
import com.rentmanagement.exception.BusinessLogicException;
import com.rentmanagement.exception.ResourceNotFoundException;
import com.rentmanagement.repository.PaymentRepository;
import com.rentmanagement.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.rentmanagement.dto.request.PaymentRequest;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TenantRepository tenantRepository;

    @Transactional
    public PaymentResponse generateMonthlyRent(Long tenantId, Integer month, Integer year) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + tenantId));

        if (tenant.getHouse() == null) {
            throw new BusinessLogicException("Tenant is not assigned to any house");
        }

        House house = tenant.getHouse();

        Payment payment = new Payment();
        payment.setMonth(month);
        payment.setYear(year);
        payment.setAmount(house.getRentAmount());
        payment.setStatus(PaymentStatus.UNPAID);
        payment.setTenant(tenant);
        payment.setHouse(house);

        Payment savedPayment = paymentRepository.save(payment);
        return convertToResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse markPaymentAsPaid(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        payment.setStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDate.now());

        Payment savedPayment = paymentRepository.save(payment);
        return convertToResponse(savedPayment);
    }

    public List<PaymentResponse> getPaymentsByTenant(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + tenantId));

        return paymentRepository.findByTenantId(tenantId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public DebtSummaryResponse calculateTenantDebt(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + tenantId));

        List<Payment> unpaidPayments = paymentRepository.findByTenantIdAndStatus(tenantId, PaymentStatus.UNPAID);
        
        Double totalDebt = unpaidPayments.stream()
                .map(Payment::getAmount)
                .reduce(0.0, Double::sum);

        return new DebtSummaryResponse(tenant.getId(), tenant.getFullName(), totalDebt);
    }

    public Double calculateTotalDebt() {
        List<Payment> unpaidPayments = paymentRepository.findByStatus(PaymentStatus.UNPAID);
        
        return unpaidPayments.stream()
                .map(Payment::getAmount)
                .reduce(0.0, Double::sum);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return convertToResponse(payment);
    }

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + request.getTenantId()));

        House house = tenant.getHouse();
        if (house == null) {
            throw new BusinessLogicException("Tenant is not assigned to any house");
        }

        Payment payment = new Payment();
        payment.setMonth(request.getMonth());
        payment.setYear(request.getYear());
        payment.setAmount(request.getAmount());
        payment.setStatus(request.getStatus() != null ? request.getStatus() : PaymentStatus.UNPAID);
        payment.setPaymentDate(request.getPaymentDate());
        payment.setTenant(tenant);
        payment.setHouse(house);

        Payment savedPayment = paymentRepository.save(payment);
        return convertToResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse updatePayment(Long id, PaymentRequest request) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + request.getTenantId()));

        House house = tenant.getHouse();
        if (house == null) {
            throw new BusinessLogicException("Tenant is not assigned to any house");
        }

        existingPayment.setMonth(request.getMonth());
        existingPayment.setYear(request.getYear());
        existingPayment.setAmount(request.getAmount());
        existingPayment.setStatus(request.getStatus());
        existingPayment.setPaymentDate(request.getPaymentDate());
        existingPayment.setTenant(tenant);
        existingPayment.setHouse(house);

        Payment updatedPayment = paymentRepository.save(existingPayment);
        return convertToResponse(updatedPayment);
    }

    @Transactional
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    public MonthlyRentSummaryResponse monthlyRentSummary(Integer month, Integer year) {
        List<Payment> monthlyPayments = paymentRepository.findByMonthAndYear(month, year);

        Double totalExpected = monthlyPayments.stream()
                .map(Payment::getAmount)
                .reduce(0.0, Double::sum);

        Double totalPaid = monthlyPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAID)
                .map(Payment::getAmount)
                .reduce(0.0, Double::sum);

        Double totalUnpaid = monthlyPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.UNPAID)
                .map(Payment::getAmount)
                .reduce(0.0, Double::sum);

        return new MonthlyRentSummaryResponse(month, year, totalExpected, totalPaid, totalUnpaid);
    }

    private PaymentResponse convertToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getMonth(),
                payment.getYear(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentDate(),
                payment.getTenant().getFullName(),
                payment.getHouse().getLocation()
        );
    }
}
