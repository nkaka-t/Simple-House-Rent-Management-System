# Complete Test Scenarios - Simple House Rent Management System

## System Information
- **Technology Stack**: Spring Boot 3.2.0, Java 17, MySQL 8.0
- **Architecture**: REST API with layered architecture (Controller → Service → Repository → Entity)
- **Testing Scope**: Backend API, Business Logic, Database Operations, Security

---

## Table of Contents
1. [Owner Management Tests](#1-owner-management-tests)
2. [House/Property Management Tests](#2-houseproperty-management-tests)
3. [Tenant Management Tests](#3-tenant-management-tests)
4. [Payment & Rent Tests](#4-payment--rent-tests)
5. [Business Rules & Logic Tests](#5-business-rules--logic-tests)
6. [API Endpoint Tests](#6-api-endpoint-tests)
7. [Database Operations Tests](#7-database-operations-tests)
8. [Security & Validation Tests](#8-security--validation-tests)
9. [Error Handling Tests](#9-error-handling-tests)
10. [Edge Cases & Concurrency Tests](#10-edge-cases--concurrency-tests)

---

## 1. Owner Management Tests

### TC-OM-001: Create Owner with Valid Data
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Create a new property owner with all valid information |
| **Endpoint** | POST /owners |
| **Prerequisites** | None |
| **Test Steps** | 1. Send POST request to /owners<br>2. Provide valid owner data in request body<br>3. Verify response |
| **Input Data** | ```json<br>{<br>  "name": "John Doe",<br>  "phone": "+1234567890",<br>  "email": "john.doe@example.com"<br>}<br>``` |
| **Expected Result** | - HTTP Status: 201 Created<br>- Response contains: id, name, phone, email<br>- Owner saved in database |
| **Validation** | - Verify owner appears in GET /owners<br>- Check database record exists |

---

### TC-OM-002: Create Owner with Missing Required Fields
**Priority**: High | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Attempt to create owner without required fields |
| **Endpoint** | POST /owners |
| **Prerequisites** | None |
| **Test Steps** | 1. Send POST request with missing name<br>2. Send POST request with missing phone<br>3. Send POST request with missing email |
| **Input Data** | ```json<br>{<br>  "phone": "+1234567890",<br>  "email": "test@example.com"<br>}<br>``` |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Error message: "Name is required"<br>- Validation error in response |

---

### TC-OM-003: Create Owner with Invalid Email Format
**Priority**: Medium | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Create owner with malformed email address |
| **Endpoint** | POST /owners |
| **Test Steps** | 1. Send POST with invalid email format |
| **Input Data** | ```json<br>{<br>  "name": "Jane Smith",<br>  "phone": "+1987654321",<br>  "email": "invalid-email"<br>}<br>``` |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Error message: "Email should be valid" |

---

### TC-OM-004: Get All Owners
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Retrieve list of all owners |
| **Endpoint** | GET /owners |
| **Prerequisites** | At least one owner exists |
| **Test Steps** | 1. Send GET request to /owners |
| **Expected Result** | - HTTP Status: 200 OK<br>- Array of owner objects returned<br>- Each owner has: id, name, phone, email |

---

### TC-OM-005: Create Multiple Owners with Same Email
**Priority**: Medium | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | Attempt to create duplicate owners with identical email |
| **Endpoint** | POST /owners |
| **Test Steps** | 1. Create first owner<br>2. Attempt to create second owner with same email |
| **Input Data** | Same email for both requests |
| **Expected Result** | - First request: 201 Created<br>- Second request: Should succeed (no unique constraint in current implementation)<br>- Note: Consider adding unique constraint |

---

## 2. House/Property Management Tests

### TC-HM-001: Create House with Valid Data
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Add a new house/property to the system |
| **Endpoint** | POST /houses |
| **Prerequisites** | Owner with ID=1 exists |
| **Test Steps** | 1. Send POST request with valid house data |
| **Input Data** | ```json<br>{<br>  "location": "123 Main St, Apt 4B",<br>  "rentAmount": 1500.00,<br>  "description": "2BR apartment",<br>  "ownerId": 1<br>}<br>``` |
| **Expected Result** | - HTTP Status: 201 Created<br>- Response includes: id, location, rentAmount, description, status=VACANT, ownerName<br>- House saved in database |

---

### TC-HM-002: Create House with Non-Existent Owner
**Priority**: High | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Attempt to create house with invalid owner ID |
| **Endpoint** | POST /houses |
| **Test Steps** | 1. Send POST with non-existent ownerId (e.g., 99999) |
| **Input Data** | ```json<br>{<br>  "location": "456 Oak Ave",<br>  "rentAmount": 2000.00,<br>  "ownerId": 99999<br>}<br>``` |
| **Expected Result** | - HTTP Status: 404 Not Found<br>- Error message: "Owner not found with id: 99999" |

---

### TC-HM-003: Create House with Invalid Rent Amount
**Priority**: Medium | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Create house with negative or zero rent amount |
| **Endpoint** | POST /houses |
| **Test Steps** | 1. Send POST with rentAmount = -500<br>2. Send POST with rentAmount = 0 |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Validation error for invalid amount |

---

### TC-HM-004: Get All Houses
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Retrieve all properties in the system |
| **Endpoint** | GET /houses |
| **Prerequisites** | At least one house exists |
| **Test Steps** | 1. Send GET request to /houses |
| **Expected Result** | - HTTP Status: 200 OK<br>- Array of house objects<br>- Each house shows: status, ownerName, tenantName (if occupied) |

---

### TC-HM-005: Get Only Vacant Houses
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Filter and retrieve only available houses |
| **Endpoint** | GET /houses/vacant |
| **Prerequisites** | Mix of vacant and occupied houses exist |
| **Test Steps** | 1. Send GET request to /houses/vacant |
| **Expected Result** | - HTTP Status: 200 OK<br>- Only houses with status=VACANT returned<br>- No occupied houses in response |

---

### TC-HM-006: Assign Tenant to House
**Priority**: Critical | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Assign an existing tenant to a vacant house |
| **Endpoint** | PUT /houses/{houseId}/assign/{tenantId} |
| **Prerequisites** | - House with ID=1 exists and is VACANT<br>- Tenant with ID=1 exists and has no house |
| **Test Steps** | 1. Send PUT request to /houses/1/assign/1 |
| **Expected Result** | - HTTP Status: 200 OK<br>- House status changed to OCCUPIED<br>- House.tenant = tenant object<br>- Tenant.house = house object<br>- Database reflects bidirectional relationship |

---

### TC-HM-007: Assign Tenant to Already Occupied House
**Priority**: Critical | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Attempt to assign tenant to house that's already occupied |
| **Endpoint** | PUT /houses/{houseId}/assign/{tenantId} |
| **Prerequisites** | - House ID=1 is OCCUPIED<br>- Tenant ID=2 exists with no house |
| **Test Steps** | 1. Send PUT request to /houses/1/assign/2 |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Error message: "House is already occupied"<br>- No changes to database |

---

### TC-HM-008: Assign Already-Assigned Tenant to Another House
**Priority**: Critical | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Try to assign tenant who already has a house to another house |
| **Endpoint** | PUT /houses/{houseId}/assign/{tenantId} |
| **Prerequisites** | - Tenant ID=1 is already assigned to House ID=1<br>- House ID=2 is VACANT |
| **Test Steps** | 1. Send PUT request to /houses/2/assign/1 |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Error message: "Tenant is already assigned to another house"<br>- No changes made |

---

### TC-HM-009: Mark House as Vacant
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Mark an occupied house as vacant (tenant leaves) |
| **Endpoint** | PUT /houses/{houseId}/vacant |
| **Prerequisites** | House ID=1 is OCCUPIED with Tenant ID=1 |
| **Test Steps** | 1. Send PUT request to /houses/1/vacant |
| **Expected Result** | - HTTP Status: 200 OK<br>- House status = VACANT<br>- House.tenant = null<br>- Tenant.house = null<br>- Bidirectional relationship cleared |

---

### TC-HM-010: Create House with Missing Location
**Priority**: High | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Attempt to create house without location |
| **Endpoint** | POST /houses |
| **Test Steps** | 1. Send POST without location field |
| **Input Data** | ```json<br>{<br>  "rentAmount": 1200.00,<br>  "ownerId": 1<br>}<br>``` |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Error: "Location is required" |

---

## 3. Tenant Management Tests

### TC-TM-001: Create Tenant with Valid Data
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Register a new tenant in the system |
| **Endpoint** | POST /tenants |
| **Test Steps** | 1. Send POST with all required tenant information |
| **Input Data** | ```json<br>{<br>  "fullName": "Alice Johnson",<br>  "phone": "+1555123456",<br>  "email": "alice.j@example.com",<br>  "nationalId": "NID123456",<br>  "startDate": "2026-03-01",<br>  "endDate": "2027-02-28"<br>}<br>``` |
| **Expected Result** | - HTTP Status: 201 Created<br>- Tenant created with all fields<br>- houseLocation = null initially |

---

### TC-TM-002: Create Tenant with Missing Required Fields
**Priority**: High | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Attempt to create tenant without mandatory fields |
| **Endpoint** | POST /tenants |
| **Test Steps** | 1. Send POST without fullName<br>2. Send POST without nationalId |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Validation errors for missing fields |

---

### TC-TM-003: Create Tenant with Invalid Email
**Priority**: Medium | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Create tenant with malformed email address |
| **Endpoint** | POST /tenants |
| **Input Data** | Email: "not-an-email" |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Error: "Email should be valid" |

---

### TC-TM-004: Get All Tenants
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Retrieve list of all registered tenants |
| **Endpoint** | GET /tenants |
| **Prerequisites** | At least one tenant exists |
| **Expected Result** | - HTTP Status: 200 OK<br>- Array of tenant objects<br>- Each shows: id, fullName, houseLocation (if assigned) |

---

### TC-TM-005: Tenant Leaves House
**Priority**: Critical | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Process tenant leaving their assigned house |
| **Endpoint** | PUT /tenants/{tenantId}/leave |
| **Prerequisites** | Tenant ID=1 is assigned to House ID=1 |
| **Test Steps** | 1. Send PUT request to /tenants/1/leave |
| **Expected Result** | - HTTP Status: 200 OK<br>- Tenant.house = null<br>- House status = VACANT<br>- House.tenant = null |

---

### TC-TM-006: Tenant Without House Tries to Leave
**Priority**: Medium | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | Attempt to process leave for tenant with no house |
| **Endpoint** | PUT /tenants/{tenantId}/leave |
| **Prerequisites** | Tenant ID=1 has no house assigned |
| **Test Steps** | 1. Send PUT request to /tenants/1/leave |
| **Expected Result** | - HTTP Status: 200 OK<br>- No errors<br>- Tenant remains unchanged |

---

### TC-TM-007: Create Tenant with Duplicate National ID
**Priority**: Medium | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | Attempt to register tenant with existing national ID |
| **Endpoint** | POST /tenants |
| **Prerequisites** | Tenant with nationalId="NID123456" exists |
| **Input Data** | Same nationalId in new tenant |
| **Expected Result** | - HTTP Status: 400/409<br>- Database constraint violation<br>- Error about duplicate national ID |

---

### TC-TM-008: Create Tenant with Invalid Date Range
**Priority**: Medium | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Create tenant where endDate is before startDate |
| **Endpoint** | POST /tenants |
| **Input Data** | startDate: "2026-12-01", endDate: "2026-01-01" |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Validation error about invalid date range |

---

## 4. Payment & Rent Tests

### TC-PM-001: Generate Monthly Rent for Tenant
**Priority**: Critical | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Generate rent payment for tenant's current month |
| **Endpoint** | POST /payments/generate/{tenantId}?month=3&year=2026 |
| **Prerequisites** | - Tenant ID=1 exists<br>- Tenant is assigned to house with rent $1500 |
| **Test Steps** | 1. Send POST to /payments/generate/1?month=3&year=2026 |
| **Expected Result** | - HTTP Status: 201 Created<br>- Payment created with:<br>  - amount = 1500.00<br>  - status = UNPAID<br>  - month = 3, year = 2026<br>  - paymentDate = null |

---

### TC-PM-002: Generate Rent for Tenant Without House
**Priority**: High | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Attempt to generate rent for unassigned tenant |
| **Endpoint** | POST /payments/generate/{tenantId}?month=3&year=2026 |
| **Prerequisites** | Tenant ID=1 has no house assigned |
| **Test Steps** | 1. Send POST to /payments/generate/1?month=3&year=2026 |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Error: "Tenant is not assigned to any house" |

---

### TC-PM-003: Mark Payment as Paid
**Priority**: Critical | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Record payment as completed |
| **Endpoint** | PUT /payments/{paymentId}/pay |
| **Prerequisites** | Payment ID=1 exists with status=UNPAID |
| **Test Steps** | 1. Send PUT to /payments/1/pay |
| **Expected Result** | - HTTP Status: 200 OK<br>- Payment status = PAID<br>- paymentDate = current date<br>- Database updated |

---

### TC-PM-004: Get All Payments for Tenant
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Retrieve payment history for specific tenant |
| **Endpoint** | GET /payments/tenant/{tenantId} |
| **Prerequisites** | Tenant ID=1 has multiple payments |
| **Test Steps** | 1. Send GET to /payments/tenant/1 |
| **Expected Result** | - HTTP Status: 200 OK<br>- Array of payment objects<br>- All payments belong to tenant ID=1 |

---

### TC-PM-005: Calculate Tenant Debt
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Calculate total unpaid rent for tenant |
| **Endpoint** | GET /payments/debt/{tenantId} |
| **Prerequisites** | Tenant ID=1 has 2 UNPAID payments ($1500 each) |
| **Test Steps** | 1. Send GET to /payments/debt/1 |
| **Expected Result** | - HTTP Status: 200 OK<br>- Response: { tenantId: 1, tenantName: "...", totalDebt: 3000.00 } |

---

### TC-PM-006: Calculate Total System Debt
**Priority**: Medium | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Sum all unpaid rent across all tenants |
| **Endpoint** | GET /payments/debt/total |
| **Prerequisites** | Multiple tenants with unpaid payments exist |
| **Test Steps** | 1. Send GET to /payments/debt/total |
| **Expected Result** | - HTTP Status: 200 OK<br>- Returns total debt amount (Double) |

---

### TC-PM-007: Monthly Rent Summary
**Priority**: High | **Type**: Functional

| Field | Details |
|-------|---------|
| **Description** | Get payment statistics for specific month |
| **Endpoint** | GET /payments/monthly-summary?month=3&year=2026 |
| **Prerequisites** | Multiple payments exist for March 2026 |
| **Test Steps** | 1. Send GET with month=3, year=2026 |
| **Expected Result** | - HTTP Status: 200 OK<br>- Response shows: month, year, totalExpected, totalPaid, totalUnpaid |

---

### TC-PM-008: Generate Duplicate Payment for Same Month
**Priority**: Medium | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | Attempt to create duplicate payment for same tenant/month |
| **Endpoint** | POST /payments/generate/{tenantId} |
| **Prerequisites** | Payment already exists for Tenant 1, March 2026 |
| **Test Steps** | 1. Generate payment for March 2026<br>2. Attempt to generate again |
| **Expected Result** | - Both requests succeed (no duplicate prevention in current implementation)<br>- Note: Consider adding constraint |

---

### TC-PM-009: Mark Non-Existent Payment as Paid
**Priority**: Medium | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Attempt to pay non-existent payment |
| **Endpoint** | PUT /payments/{paymentId}/pay |
| **Test Steps** | 1. Send PUT to /payments/99999/pay |
| **Expected Result** | - HTTP Status: 404 Not Found<br>- Error: "Payment not found with id: 99999" |

---

### TC-PM-010: Generate Rent with Invalid Month
**Priority**: Low | **Type**: Negative

| Field | Details |
|-------|---------|
| **Description** | Generate payment with invalid month value |
| **Endpoint** | POST /payments/generate/{tenantId}?month=13&year=2026 |
| **Test Steps** | 1. Send POST with month=13 or month=0 |
| **Expected Result** | - HTTP Status: 400 Bad Request<br>- Validation error for invalid month |

---

## 5. Business Rules & Logic Tests

### TC-BR-001: Automatic House Status Change on Assignment
**Priority**: Critical | **Type**: Business Rule

| Field | Details |
|-------|---------|
| **Description** | Verify house status automatically changes to OCCUPIED when tenant assigned |
| **Test Steps** | 1. Verify house is VACANT<br>2. Assign tenant<br>3. Verify house status is OCCUPIED |
| **Expected Result** | House status automatically updated without explicit status change API call |

---

### TC-BR-002: Automatic House Status Change on Tenant Leave
**Priority**: Critical | **Type**: Business Rule

| Field | Details |
|-------|---------|
| **Description** | Verify house becomes VACANT when tenant leaves |
| **Test Steps** | 1. Tenant is in OCCUPIED house<br>2. Call tenant leave API<br>3. Verify house is VACANT |
| **Expected Result** | House status = VACANT, tenant unlinked |

---

### TC-BR-003: Bidirectional Relationship Integrity
**Priority**: Critical | **Type**: Business Rule

| Field | Details |
|-------|---------|
| **Description** | Ensure tenant-house relationship is maintained bidirectionally |
| **Test Steps** | 1. Assign tenant to house<br>2. Check house.tenant<br>3. Check tenant.house |
| **Expected Result** | Both sides of relationship are correctly set |

---

### TC-BR-004: Payment Amount Equals House Rent
**Priority**: High | **Type**: Business Rule

| Field | Details |
|-------|---------|
| **Description** | Generated payment amount matches house rent |
| **Test Steps** | 1. House has rent $1800<br>2. Generate payment for tenant<br>3. Verify payment amount |
| **Expected Result** | Payment.amount = 1800.00 |

---

### TC-BR-005: Single House Assignment Rule
**Priority**: Critical | **Type**: Business Rule

| Field | Details |
|-------|---------|
| **Description** | Tenant cannot be assigned to multiple houses |
| **Test Steps** | 1. Assign tenant to house 1<br>2. Attempt to assign same tenant to house 2 |
| **Expected Result** | Second assignment fails with error |

---

### TC-BR-006: Prevent Assignment to Occupied House
**Priority**: Critical | **Type**: Business Rule

| Field | Details |
|-------|---------|
| **Description** | Cannot assign tenant to already occupied house |
| **Test Steps** | 1. House 1 has tenant A<br>2. Attempt to assign tenant B to house 1 |
| **Expected Result** | Assignment fails with "House is already occupied" |

---

### TC-BR-007: Debt Calculation Accuracy
**Priority**: High | **Type**: Business Rule

| Field | Details |
|-------|---------|
| **Description** | Debt only includes UNPAID payments |
| **Test Steps** | 1. Tenant has 3 payments: 2 UNPAID ($1500 each), 1 PAID ($1500)<br>2. Get tenant debt |
| **Expected Result** | Total debt = 3000.00 (not 4500) |

---

## 6. API Endpoint Tests

### TC-API-001: All Endpoints Return Correct HTTP Methods
**Priority**: High | **Type**: API

| Endpoint | Method | Expected Status |
|----------|--------|-----------------|
| POST /owners | POST | 201 |
| GET /owners | GET | 200 |
| POST /houses | POST | 201 |
| GET /houses | GET | 200 |
| PUT /houses/{id}/assign/{tenantId} | PUT | 200 |
| PUT /houses/{id}/vacant | PUT | 200 |
| GET /houses/vacant | GET | 200 |
| POST /tenants | POST | 201 |
| GET /tenants | GET | 200 |
| PUT /tenants/{id}/leave | PUT | 200 |
| POST /payments/generate/{tenantId} | POST | 201 |
| PUT /payments/{id}/pay | PUT | 200 |
| GET /payments/tenant/{tenantId} | GET | 200 |
| GET /payments/debt/{tenantId} | GET | 200 |
| GET /payments/debt/total | GET | 200 |
| GET /payments/monthly-summary | GET | 200 |

---

### TC-API-002: Invalid HTTP Method Returns 405
**Priority**: Medium | **Type**: API

| Field | Details |
|-------|---------|
| **Description** | Using wrong HTTP method returns Method Not Allowed |
| **Test Steps** | 1. Send DELETE to GET-only endpoint<br>2. Send GET to POST-only endpoint |
| **Expected Result** | HTTP Status: 405 Method Not Allowed |

---

### TC-API-003: Non-Existent Endpoint Returns 404
**Priority**: Medium | **Type**: API

| Field | Details |
|-------|---------|
| **Description** | Accessing undefined endpoint returns Not Found |
| **Test Steps** | 1. Send request to /api/nonexistent |
| **Expected Result** | HTTP Status: 404 Not Found |

---

### TC-API-004: Content-Type Validation
**Priority**: Medium | **Type**: API

| Field | Details |
|-------|---------|
| **Description** | Endpoints accept application/json |
| **Test Steps** | 1. Send POST request with text/plain<br>2. Send POST with application/json |
| **Expected Result** | - text/plain: 415 Unsupported Media Type<br>- application/json: Accepted |

---

### TC-API-005: Response Format Consistency
**Priority**: High | **Type**: API

| Field | Details |
|-------|---------|
| **Description** | All responses return proper JSON format |
| **Test Steps** | 1. Call any endpoint<br>2. Verify response is valid JSON |
| **Expected Result** | All responses parseable as JSON |

---

## 7. Database Operations Tests

### TC-DB-001: Create Operations Persist Data
**Priority**: Critical | **Type**: Database

| Field | Details |
|-------|---------|
| **Description** | Data created via API is saved in database |
| **Test Steps** | 1. Create owner via API<br>2. Query database directly<br>3. Verify record exists |
| **Expected Result** | Database contains matching record |

---

### TC-DB-002: Update Operations Modify Database
**Priority**: Critical | **Type**: Database

| Field | Details |
|-------|---------|
| **Description** | Updates reflect in database |
| **Test Steps** | 1. Mark payment as paid<br>2. Query database<br>3. Check status and paymentDate |
| **Expected Result** | Database shows status=PAID, paymentDate set |

---

### TC-DB-003: Delete Operations Remove Data
**Priority**: High | **Type**: Database

| Field | Details |
|-------|---------|
| **Description** | Deletion removes records from database |
| **Test Steps** | 1. Note: Current API doesn't have delete endpoints<br>2. Consider adding for completeness |
| **Expected Result** | N/A - Feature not implemented |

---

### TC-DB-004: Cascade Operations Work Correctly
**Priority**: High | **Type**: Database

| Field | Details |
|-------|---------|
| **Description** | Related entities handle cascading correctly |
| **Test Steps** | 1. Check if deleting owner affects houses<br>2. Verify cascade behavior |
| **Expected Result** | Cascade rules (CascadeType.ALL) work as defined |

---

### TC-DB-005: Transaction Rollback on Error
**Priority**: Critical | **Type**: Database

| Field | Details |
|-------|---------|
| **Description** | Failed operations don't leave partial data |
| **Test Steps** | 1. Start complex operation that will fail<br>2. Verify no partial data exists |
| **Expected Result** | @Transactional ensures atomicity |

---

### TC-DB-006: Unique Constraints Enforced
**Priority**: High | **Type**: Database

| Field | Details |
|-------|---------|
| **Description** | Database enforces unique constraints |
| **Test Steps** | 1. Create tenant with nationalId "ABC123"<br>2. Attempt duplicate nationalId |
| **Expected Result** | Second insert fails with constraint violation |

---

### TC-DB-007: Foreign Key Integrity
**Priority**: Critical | **Type**: Database

| Field | Details |
|-------|---------|
| **Description** | Foreign keys maintain referential integrity |
| **Test Steps** | 1. Attempt to create house with ownerId=99999<br>2. Verify rejection |
| **Expected Result** | Operation fails before database constraint violation |

---

## 8. Security & Validation Tests

### TC-SEC-001: SQL Injection Prevention
**Priority**: Critical | **Type**: Security

| Field | Details |
|-------|---------|
| **Description** | System prevents SQL injection attacks |
| **Test Steps** | 1. Send malicious SQL in input fields<br>2. Example: name = "'; DROP TABLE owners;--" |
| **Expected Result** | - Input treated as literal string<br>- No SQL execution<br>- Validation may reject |

---

### TC-SEC-002: XSS Prevention in Input
**Priority**: High | **Type**: Security

| Field | Details |
|-------|---------|
| **Description** | System sanitizes script tags in input |
| **Test Steps** | 1. Create owner with name containing `<script>alert('XSS')</script>` |
| **Expected Result** | - Input stored as-is or sanitized<br>- No script execution on retrieval |

---

### TC-SEC-003: Input Length Validation
**Priority**: Medium | **Type**: Security

| Field | Details |
|-------|---------|
| **Description** | Prevent buffer overflow with extremely long inputs |
| **Test Steps** | 1. Send 10,000 character string for name field |
| **Expected Result** | - Rejected by validation<br>- Or truncated to max length |

---

### TC-SEC-004: Special Characters Handling
**Priority**: Medium | **Type**: Security

| Field | Details |
|-------|---------|
| **Description** | System handles special characters safely |
| **Test Steps** | 1. Create owner with name: "O'Brien & Sons <Ltd>"<br>2. Verify correct storage/retrieval |
| **Expected Result** | Special characters preserved correctly |

---

### TC-SEC-005: Null/Empty Input Validation
**Priority**: High | **Type**: Validation

| Field | Details |
|-------|---------|
| **Description** | Required fields reject null or empty strings |
| **Test Steps** | 1. Send POST with empty string ""<br>2. Send POST with null value |
| **Expected Result** | HTTP 400 with validation error |

---

### TC-SEC-006: Data Type Validation
**Priority**: High | **Type**: Validation

| Field | Details |
|-------|---------|
| **Description** | Incorrect data types are rejected |
| **Test Steps** | 1. Send string for rentAmount (numeric field)<br>2. Send number for name (string field) |
| **Expected Result** | - HTTP 400 Bad Request<br>- Type mismatch error |

---

### TC-SEC-007: Negative Number Validation
**Priority**: Medium | **Type**: Validation

| Field | Details |
|-------|---------|
| **Description** | Negative values rejected for positive-only fields |
| **Test Steps** | 1. Create house with rentAmount = -1000 |
| **Expected Result** | Validation error (add @Positive constraint if needed) |

---

### TC-SEC-008: Future Date Validation
**Priority**: Low | **Type**: Validation

| Field | Details |
|-------|---------|
| **Description** | Payment date cannot be in future |
| **Test Steps** | 1. Attempt to manually set paymentDate to future date |
| **Expected Result** | System prevents or validates |

---

## 9. Error Handling Tests

### TC-ERR-001: 404 for Non-Existent Resources
**Priority**: High | **Type**: Error Handling

| Field | Details |
|-------|---------|
| **Description** | Proper 404 response for missing resources |
| **Test Steps** | 1. GET /owners/99999<br>2. PUT /houses/99999/vacant |
| **Expected Result** | - HTTP Status: 404<br>- JSON error response<br>- Message: "Resource not found..." |

---

### TC-ERR-002: 400 for Validation Errors
**Priority**: High | **Type**: Error Handling

| Field | Details |
|-------|---------|
| **Description** | Validation failures return 400 with details |
| **Test Steps** | 1. Send invalid data to any POST endpoint |
| **Expected Result** | - HTTP Status: 400<br>- Error object with field-level details |

---

### TC-ERR-003: 400 for Business Logic Violations
**Priority**: High | **Type**: Error Handling

| Field | Details |
|-------|---------|
| **Description** | Business rule violations return clear errors |
| **Test Steps** | 1. Assign tenant to occupied house |
| **Expected Result** | - HTTP 400<br>- Error: "House is already occupied" |

---

### TC-ERR-004: 500 for Server Errors
**Priority**: High | **Type**: Error Handling

| Field | Details |
|-------|---------|
| **Description** | Unexpected errors return 500 with generic message |
| **Test Steps** | 1. Simulate database connection failure<br>2. Trigger unhandled exception |
| **Expected Result** | - HTTP Status: 500<br>- Generic error message<br>- No stack trace exposed |

---

### TC-ERR-005: Error Response Format Consistency
**Priority**: Medium | **Type**: Error Handling

| Field | Details |
|-------|---------|
| **Description** | All errors follow same JSON structure |
| **Expected Result** | ```json<br>{<br>  "timestamp": "2026-02-21T...",<br>  "status": 400,<br>  "error": "Bad Request",<br>  "message": "...",<br>  "path": "/owners"<br>}<br>``` |

---

### TC-ERR-006: Validation Error Details
**Priority**: High | **Type**: Error Handling

| Field | Details |
|-------|---------|
| **Description** | Validation errors show all field errors |
| **Test Steps** | 1. Submit owner with missing name AND invalid email |
| **Expected Result** | Response shows errors for both fields |

---

## 10. Edge Cases & Concurrency Tests

### TC-EDGE-001: Empty Database Queries
**Priority**: Medium | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | GET requests work when no data exists |
| **Test Steps** | 1. Call GET /owners on fresh database |
| **Expected Result** | - HTTP Status: 200 OK<br>- Empty array [] returned |

---

### TC-EDGE-002: Very Long String Inputs
**Priority**: Low | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | Handle inputs at or beyond reasonable limits |
| **Test Steps** | 1. Create owner with 1000-character name |
| **Expected Result** | - Accepted and stored OR<br>- Validation error if max length defined |

---

### TC-EDGE-003: Concurrent House Assignment
**Priority**: Critical | **Type**: Concurrency

| Field | Details |
|-------|---------|
| **Description** | Two users try to assign different tenants to same house simultaneously |
| **Test Steps** | 1. User A: PUT /houses/1/assign/1<br>2. User B (simultaneously): PUT /houses/1/assign/2 |
| **Expected Result** | - One succeeds (200 OK)<br>- One fails (400 - already occupied)<br>- No race condition |

---

### TC-EDGE-004: Concurrent Payment Generation
**Priority**: Medium | **Type**: Concurrency

| Field | Details |
|-------|---------|
| **Description** | Simultaneous payment generation for same tenant/month |
| **Test Steps** | 1. Generate March payment twice simultaneously |
| **Expected Result** | - Both may succeed (no uniqueness constraint)<br>- Consider adding constraint |

---

### TC-EDGE-005: Special Characters in All Fields
**Priority**: Low | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | Unicode and special characters in inputs |
| **Test Steps** | 1. Create owner with name: "José García-López 李明" |
| **Expected Result** | - Accepted and stored correctly<br>- Retrieved without corruption |

---

### TC-EDGE-006: Boundary Values for Dates
**Priority**: Low | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | Edge cases for date fields |
| **Test Steps** | 1. Start date = today<br>2. End date = 100 years in future<br>3. Invalid dates like Feb 30 |
| **Expected Result** | - Valid dates accepted<br>- Invalid dates rejected |

---

### TC-EDGE-007: Extremely Large Rent Amount
**Priority**: Low | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | Handle very large monetary values |
| **Test Steps** | 1. Create house with rentAmount = 999999999.99 |
| **Expected Result** | - Accepted if within Double range<br>- Or validation error if max defined |

---

### TC-EDGE-008: Zero Rent Amount
**Priority**: Medium | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | Handle free housing scenario |
| **Test Steps** | 1. Create house with rentAmount = 0.00 |
| **Expected Result** | - May be valid business case<br>- Or validation rejects (add @Positive) |

---

### TC-EDGE-009: Multiple Tenants Leave Simultaneously
**Priority**: Low | **Type**: Concurrency

| Field | Details |
|-------|---------|
| **Description** | Concurrent leave operations |
| **Test Steps** | 1. Two API calls: tenant 1 leave, tenant 2 leave (same time) |
| **Expected Result** | - Both complete successfully<br>- No race conditions |

---

### TC-EDGE-010: Database Connection Loss During Operation
**Priority**: High | **Type**: Edge Case

| Field | Details |
|-------|---------|
| **Description** | Handle database connectivity issues |
| **Test Steps** | 1. Simulate DB connection loss mid-operation |
| **Expected Result** | - HTTP 500 or 503<br>- No data corruption<br>- Clear error message |

---

## Test Execution Guidelines

### Prerequisites
1. MySQL database running on localhost:3306
2. Database 'rent_management' created
3. Application running on localhost:8080
4. Test tools: Postman, cURL, or automated test framework

### Test Data Setup
```sql
-- Sample test data
INSERT INTO owners (name, phone, email) VALUES ('Test Owner', '+1234567890', 'test@owner.com');
INSERT INTO houses (location, rent_amount, description, status, owner_id) VALUES ('123 Test St', 1500.00, 'Test House', 'VACANT', 1);
INSERT INTO tenants (full_name, phone, email, national_id, start_date) VALUES ('Test Tenant', '+1987654321', 'test@tenant.com', 'TEST123', '2026-01-01');
```

### Test Execution Order
1. **Smoke Tests**: Basic CRUD for each entity
2. **Functional Tests**: Business logic and workflows
3. **Integration Tests**: End-to-end scenarios
4. **Negative Tests**: Error handling and validation
5. **Edge Cases**: Boundary conditions
6. **Performance Tests**: Load and stress testing (if needed)

### Pass/Fail Criteria
- **Pass**: Expected result matches actual result
- **Fail**: Any deviation from expected result
- **Blocked**: Cannot execute due to dependency failure
- **Skip**: Not applicable to current build

---

## Test Metrics to Track
- Total test cases: 100+
- Pass rate target: >95%
- Critical bugs: 0
- High priority bugs: <3
- Code coverage: >80%

---

## Automation Recommendations
Consider automating tests using:
- **JUnit 5** + **MockMvc** for unit/integration tests
- **RestAssured** for API testing
- **Testcontainers** for database integration tests
- **JaCoCo** for code coverage reporting

---

*Document Version: 1.0*  
*Last Updated: February 21, 2026*  
*Total Test Scenarios: 100+*
