# Automated Test Templates for JUnit 5

This document provides test code templates for automated testing of the House Rent Management System.

## Table of Contents
1. [Unit Test Templates](#unit-test-templates)
2. [Integration Test Templates](#integration-test-templates)
3. [API Test Templates with MockMvc](#api-test-templates-with-mockmvc)
4. [Test Configuration](#test-configuration)

---

## Unit Test Templates

### Service Layer Test Template

```java
package com.rentmanagement.service;

import com.rentmanagement.dto.request.OwnerRequest;
import com.rentmanagement.dto.response.OwnerResponse;
import com.rentmanagement.entity.Owner;
import com.rentmanagement.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    private Owner owner;
    private OwnerRequest ownerRequest;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        owner.setId(1L);
        owner.setName("John Doe");
        owner.setPhone("+1234567890");
        owner.setEmail("john@example.com");

        ownerRequest = new OwnerRequest();
        ownerRequest.setName("John Doe");
        ownerRequest.setPhone("+1234567890");
        ownerRequest.setEmail("john@example.com");
    }

    @Test
    void testCreateOwner_Success() {
        // Arrange
        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

        // Act
        OwnerResponse response = ownerService.createOwner(ownerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("+1234567890", response.getPhone());
        assertEquals("john@example.com", response.getEmail());
        verify(ownerRepository, times(1)).save(any(Owner.class));
    }

    @Test
    void testGetAllOwners_ReturnsOwnerList() {
        // Arrange
        List<Owner> owners = Arrays.asList(owner);
        when(ownerRepository.findAll()).thenReturn(owners);

        // Act
        List<OwnerResponse> responses = ownerService.getAllOwners();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("John Doe", responses.get(0).getName());
        verify(ownerRepository, times(1)).findAll();
    }
}
```

---

### Business Logic Test Template

```java
package com.rentmanagement.service;

import com.rentmanagement.entity.House;
import com.rentmanagement.entity.Tenant;
import com.rentmanagement.enums.HouseStatus;
import com.rentmanagement.exception.BusinessLogicException;
import com.rentmanagement.repository.HouseRepository;
import com.rentmanagement.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HouseServiceTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private HouseService houseService;

    private House vacantHouse;
    private House occupiedHouse;
    private Tenant tenant;

    @BeforeEach
    void setUp() {
        vacantHouse = new House();
        vacantHouse.setId(1L);
        vacantHouse.setLocation("123 Main St");
        vacantHouse.setRentAmount(1500.0);
        vacantHouse.setStatus(HouseStatus.VACANT);

        occupiedHouse = new House();
        occupiedHouse.setId(2L);
        occupiedHouse.setLocation("456 Oak Ave");
        occupiedHouse.setRentAmount(2000.0);
        occupiedHouse.setStatus(HouseStatus.OCCUPIED);

        tenant = new Tenant();
        tenant.setId(1L);
        tenant.setFullName("Alice Johnson");
    }

    @Test
    void testAssignTenantToHouse_Success() {
        // Arrange
        when(houseRepository.findById(1L)).thenReturn(Optional.of(vacantHouse));
        when(tenantRepository.findById(1L)).thenReturn(Optional.of(tenant));
        when(houseRepository.save(any(House.class))).thenReturn(vacantHouse);
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        // Act
        houseService.assignTenantToHouse(1L, 1L);

        // Assert
        assertEquals(HouseStatus.OCCUPIED, vacantHouse.getStatus());
        assertEquals(tenant, vacantHouse.getTenant());
        assertEquals(vacantHouse, tenant.getHouse());
        verify(houseRepository, times(1)).save(vacantHouse);
        verify(tenantRepository, times(1)).save(tenant);
    }

    @Test
    void testAssignTenantToOccupiedHouse_ThrowsException() {
        // Arrange
        when(houseRepository.findById(2L)).thenReturn(Optional.of(occupiedHouse));
        when(tenantRepository.findById(1L)).thenReturn(Optional.of(tenant));

        // Act & Assert
        BusinessLogicException exception = assertThrows(
            BusinessLogicException.class,
            () -> houseService.assignTenantToHouse(2L, 1L)
        );

        assertEquals("House is already occupied", exception.getMessage());
        verify(houseRepository, never()).save(any(House.class));
    }

    @Test
    void testAssignAlreadyAssignedTenant_ThrowsException() {
        // Arrange
        tenant.setHouse(occupiedHouse);
        when(houseRepository.findById(1L)).thenReturn(Optional.of(vacantHouse));
        when(tenantRepository.findById(1L)).thenReturn(Optional.of(tenant));

        // Act & Assert
        BusinessLogicException exception = assertThrows(
            BusinessLogicException.class,
            () -> houseService.assignTenantToHouse(1L, 1L)
        );

        assertEquals("Tenant is already assigned to another house", exception.getMessage());
    }

    @Test
    void testMarkHouseVacant_Success() {
        // Arrange
        occupiedHouse.setTenant(tenant);
        tenant.setHouse(occupiedHouse);
        when(houseRepository.findById(2L)).thenReturn(Optional.of(occupiedHouse));
        when(houseRepository.save(any(House.class))).thenReturn(occupiedHouse);

        // Act
        houseService.markHouseVacant(2L);

        // Assert
        assertEquals(HouseStatus.VACANT, occupiedHouse.getStatus());
        assertNull(occupiedHouse.getTenant());
        assertNull(tenant.getHouse());
        verify(houseRepository, times(1)).save(occupiedHouse);
    }
}
```

---

## Integration Test Templates

### Repository Integration Test

```java
package com.rentmanagement.repository;

import com.rentmanagement.entity.Owner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OwnerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void testSaveOwner_Success() {
        // Arrange
        Owner owner = new Owner();
        owner.setName("John Doe");
        owner.setPhone("+1234567890");
        owner.setEmail("john@example.com");

        // Act
        Owner savedOwner = ownerRepository.save(owner);

        // Assert
        assertNotNull(savedOwner.getId());
        assertEquals("John Doe", savedOwner.getName());

        // Verify in database
        Owner foundOwner = entityManager.find(Owner.class, savedOwner.getId());
        assertNotNull(foundOwner);
        assertEquals("John Doe", foundOwner.getName());
    }

    @Test
    void testFindById_Success() {
        // Arrange
        Owner owner = new Owner();
        owner.setName("Jane Smith");
        owner.setPhone("+1987654321");
        owner.setEmail("jane@example.com");
        Owner persistedOwner = entityManager.persistAndFlush(owner);

        // Act
        Optional<Owner> foundOwner = ownerRepository.findById(persistedOwner.getId());

        // Assert
        assertTrue(foundOwner.isPresent());
        assertEquals("Jane Smith", foundOwner.get().getName());
    }
}
```

---

## API Test Templates with MockMvc

### Controller Test Template

```java
package com.rentmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentmanagement.dto.request.OwnerRequest;
import com.rentmanagement.dto.response.OwnerResponse;
import com.rentmanagement.service.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OwnerService ownerService;

    private OwnerRequest ownerRequest;
    private OwnerResponse ownerResponse;

    @BeforeEach
    void setUp() {
        ownerRequest = new OwnerRequest("John Doe", "+1234567890", "john@example.com");

        ownerResponse = new OwnerResponse();
        ownerResponse.setId(1L);
        ownerResponse.setName("John Doe");
        ownerResponse.setPhone("+1234567890");
        ownerResponse.setEmail("john@example.com");
    }

    @Test
    void testCreateOwner_Success() throws Exception {
        // Arrange
        when(ownerService.createOwner(any(OwnerRequest.class))).thenReturn(ownerResponse);

        // Act & Assert
        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ownerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.phone").value("+1234567890"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testCreateOwner_InvalidEmail_ReturnsBadRequest() throws Exception {
        // Arrange
        OwnerRequest invalidRequest = new OwnerRequest("John Doe", "+1234567890", "invalid-email");

        // Act & Assert
        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateOwner_MissingName_ReturnsBadRequest() throws Exception {
        // Arrange
        OwnerRequest invalidRequest = new OwnerRequest(null, "+1234567890", "test@example.com");

        // Act & Assert
        mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllOwners_ReturnsOwnerList() throws Exception {
        // Arrange
        List<OwnerResponse> owners = Arrays.asList(ownerResponse);
        when(ownerService.getAllOwners()).thenReturn(owners);

        // Act & Assert
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }
}
```

---

### Full Integration Test

```java
package com.rentmanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentmanagement.dto.request.HouseRequest;
import com.rentmanagement.dto.request.OwnerRequest;
import com.rentmanagement.dto.request.TenantRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class CompleteRentalFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCompleteRentalFlow_Success() throws Exception {
        // Step 1: Create Owner
        OwnerRequest ownerRequest = new OwnerRequest("Test Owner", "+1234567890", "owner@test.com");
        MvcResult ownerResult = mockMvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ownerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Long ownerId = objectMapper.readTree(ownerResult.getResponse().getContentAsString())
                .get("id").asLong();

        // Step 2: Create House
        HouseRequest houseRequest = new HouseRequest();
        houseRequest.setLocation("123 Test St");
        houseRequest.setRentAmount(1500.0);
        houseRequest.setDescription("Test House");
        houseRequest.setOwnerId(ownerId);

        MvcResult houseResult = mockMvc.perform(post("/houses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(houseRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("VACANT"))
                .andReturn();

        Long houseId = objectMapper.readTree(houseResult.getResponse().getContentAsString())
                .get("id").asLong();

        // Step 3: Create Tenant
        TenantRequest tenantRequest = new TenantRequest();
        tenantRequest.setFullName("Test Tenant");
        tenantRequest.setPhone("+1987654321");
        tenantRequest.setEmail("tenant@test.com");
        tenantRequest.setNationalId("TEST123");
        tenantRequest.setStartDate(LocalDate.now());

        MvcResult tenantResult = mockMvc.perform(post("/tenants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tenantRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Long tenantId = objectMapper.readTree(tenantResult.getResponse().getContentAsString())
                .get("id").asLong();

        // Step 4: Assign Tenant to House
        mockMvc.perform(put("/houses/{houseId}/assign/{tenantId}", houseId, tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OCCUPIED"));

        // Step 5: Generate Payment
        MvcResult paymentResult = mockMvc.perform(post("/payments/generate/{tenantId}", tenantId)
                .param("month", "3")
                .param("year", "2026"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("UNPAID"))
                .andExpect(jsonPath("$.amount").value(1500.0))
                .andReturn();

        Long paymentId = objectMapper.readTree(paymentResult.getResponse().getContentAsString())
                .get("id").asLong();

        // Step 6: Mark Payment as Paid
        mockMvc.perform(put("/payments/{paymentId}/pay", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.paymentDate").isNotEmpty());

        // Step 7: Verify Debt is 0
        mockMvc.perform(get("/payments/debt/{tenantId}", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDebt").value(0.0));
    }
}
```

---

## Test Configuration

### application-test.properties

```properties
# H2 In-Memory Database for Testing
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Disable Hibernate SQL logging in tests
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Test Dependencies (pom.xml)

```xml
<dependencies>
    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- Mockito -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- Spring Boot Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- H2 Database for Testing -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- AssertJ -->
    <dependency>
        <groupId>org.assertj</groupId>
        <artifactId>assertj-core</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=OwnerServiceTest

# Run tests with coverage
mvn test jacoco:report

# Run integration tests only
mvn verify -P integration-tests
```

---

*Automated Test Templates Version: 1.0*  
*For manual test scenarios, see TEST_SCENARIOS.md*
