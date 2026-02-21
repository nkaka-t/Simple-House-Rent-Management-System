package com.rentmanagement.repository;

import com.rentmanagement.entity.Payment;
import com.rentmanagement.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByTenantId(Long tenantId);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByTenantIdAndStatus(Long tenantId, PaymentStatus status);
    List<Payment> findByMonthAndYear(Integer month, Integer year);
}
