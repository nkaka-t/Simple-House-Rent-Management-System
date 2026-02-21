package com.rentmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Phone is required")
    @Column(nullable = false)
    private String phone;

    @Email(message = "Email should be valid")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "National ID is required")
    @Column(nullable = false, unique = true)
    private String nationalId;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;
}
