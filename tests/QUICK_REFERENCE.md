# Test Execution Checklist & Quick Reference

## Quick Test Matrix

| Module | Total Tests | Priority | Status |
|--------|-------------|----------|--------|
| Owner Management | 5 | High | ⬜ |
| House Management | 10 | Critical | ⬜ |
| Tenant Management | 8 | High | ⬜ |
| Payment & Rent | 10 | Critical | ⬜ |
| Business Rules | 7 | Critical | ⬜ |
| API Endpoints | 5 | High | ⬜ |
| Database Operations | 7 | High | ⬜ |
| Security & Validation | 8 | Critical | ⬜ |
| Error Handling | 6 | High | ⬜ |
| Edge Cases & Concurrency | 10 | Medium | ⬜ |
| **TOTAL** | **76+** | | |

---

## Priority-Based Test Execution

### 🔴 Critical Priority (Must Pass Before Release)
- TC-HM-006: Assign Tenant to House
- TC-HM-007: Prevent Assignment to Occupied House
- TC-HM-008: Prevent Double Assignment of Tenant
- TC-TM-005: Tenant Leaves House
- TC-PM-001: Generate Monthly Rent
- TC-PM-003: Mark Payment as Paid
- TC-BR-001 to TC-BR-006: All Business Rules
- TC-DB-001: Create Operations Persist
- TC-DB-005: Transaction Rollback
- TC-SEC-001: SQL Injection Prevention
- TC-EDGE-003: Concurrent House Assignment

### 🟡 High Priority (Should Pass)
- All Owner Management Tests
- All Tenant Management Tests
- All Payment Calculation Tests
- API Endpoint Tests
- Database Integrity Tests
- Security Validation Tests

### 🟢 Medium/Low Priority (Nice to Have)
- Edge Cases with Special Characters
- Extreme Value Testing
- Performance/Load Testing

---

## Test Environment Setup

### 1. Database Setup
```sql
-- Create database
CREATE DATABASE IF NOT EXISTS rent_management;
USE rent_management;

-- Verify tables are created (run app first to auto-create schema)
SHOW TABLES;

-- Clear test data between runs
TRUNCATE TABLE payments;
TRUNCATE TABLE tenants;
TRUNCATE TABLE houses;
TRUNCATE TABLE owners;
```

### 2. Application Startup
```bash
cd c:\projects\Teta\simple-house-rent-management-system
mvn spring-boot:run
```

### 3. Verify Application is Running
```bash
curl http://localhost:8080/owners
# Expected: [] or list of owners
```

---

## Quick cURL Commands for Smoke Testing

### Create Owner
```bash
curl -X POST http://localhost:8080/owners \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","phone":"+1234567890","email":"john@test.com"}'
```

### Create House
```bash
curl -X POST http://localhost:8080/houses \
  -H "Content-Type: application/json" \
  -d '{"location":"123 Main St","rentAmount":1500.00,"description":"2BR Apt","ownerId":1}'
```

### Create Tenant
```bash
curl -X POST http://localhost:8080/tenants \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Alice Smith","phone":"+1555123456","email":"alice@test.com","nationalId":"NID001","startDate":"2026-03-01"}'
```

### Assign Tenant to House
```bash
curl -X PUT http://localhost:8080/houses/1/assign/1
```

### Generate Payment
```bash
curl -X POST "http://localhost:8080/payments/generate/1?month=3&year=2026"
```

### Mark Payment as Paid
```bash
curl -X PUT http://localhost:8080/payments/1/pay
```

### Get Tenant Debt
```bash
curl http://localhost:8080/payments/debt/1
```

---

## Test Data Templates

### Valid Owner
```json
{
  "name": "Test Owner Name",
  "phone": "+1234567890",
  "email": "valid.email@example.com"
}
```

### Valid House
```json
{
  "location": "123 Test Street, Apt 4B",
  "rentAmount": 1500.00,
  "description": "2 bedroom apartment with parking",
  "ownerId": 1
}
```

### Valid Tenant
```json
{
  "fullName": "Test Tenant Name",
  "phone": "+1987654321",
  "email": "tenant@example.com",
  "nationalId": "NID123456",
  "startDate": "2026-03-01",
  "endDate": "2027-02-28"
}
```

### Invalid Data Examples

**Missing Required Field**
```json
{
  "phone": "+1234567890",
  "email": "test@example.com"
  // Missing "name"
}
```

**Invalid Email**
```json
{
  "name": "Test User",
  "phone": "+1234567890",
  "email": "not-an-email"
}
```

**Negative Rent Amount**
```json
{
  "location": "Test Location",
  "rentAmount": -500.00,
  "ownerId": 1
}
```

---

## Common Test Scenarios Workflow

### Scenario 1: Complete Rental Flow
1. Create Owner (POST /owners)
2. Create House for Owner (POST /houses)
3. Verify House is VACANT (GET /houses)
4. Create Tenant (POST /tenants)
5. Assign Tenant to House (PUT /houses/{id}/assign/{tenantId})
6. Verify House is OCCUPIED (GET /houses)
7. Generate Monthly Rent (POST /payments/generate/{tenantId})
8. Verify Payment is UNPAID (GET /payments/tenant/{tenantId})
9. Mark Payment as PAID (PUT /payments/{id}/pay)
10. Calculate Debt (should be 0) (GET /payments/debt/{tenantId})

### Scenario 2: Tenant Leaving Flow
1. Tenant is assigned to House (OCCUPIED)
2. Tenant Leaves (PUT /tenants/{id}/leave)
3. Verify House is VACANT (GET /houses)
4. Verify Tenant has no house (GET /tenants)

### Scenario 3: Business Rule Violation
1. House 1 is OCCUPIED by Tenant A
2. Try to assign Tenant B to House 1
3. Should fail with 400 error

---

## Expected HTTP Status Codes Reference

| Status Code | Meaning | When to Expect |
|-------------|---------|----------------|
| 200 OK | Success | GET, PUT operations |
| 201 Created | Resource Created | POST operations |
| 400 Bad Request | Validation Error | Invalid data, business rule violation |
| 404 Not Found | Resource Missing | Non-existent ID in request |
| 405 Method Not Allowed | Wrong HTTP Method | Using GET on POST endpoint |
| 500 Internal Server Error | Server Error | Unexpected system error |

---

## Test Execution Log Template

```
Test Run: [Date/Time]
Tester: [Name]
Environment: localhost:8080
Database: rent_management

| Test ID | Description | Status | Notes |
|---------|-------------|--------|-------|
| TC-OM-001 | Create Owner Valid | ✅ Pass | |
| TC-OM-002 | Create Owner Missing Fields | ✅ Pass | |
| TC-HM-001 | Create House Valid | ✅ Pass | |
| TC-HM-006 | Assign Tenant to House | ❌ Fail | Error: timeout |
| ... | ... | ... | ... |

Summary:
- Total Tests Run: X
- Passed: Y
- Failed: Z
- Pass Rate: Y/X%
- Critical Failures: N
```

---

## Debugging Tips

### If Test Fails:
1. Check application logs: `logs/spring.log`
2. Verify database state: `SELECT * FROM [table]`
3. Check request/response in browser network tab
4. Verify correct endpoint and HTTP method
5. Validate JSON syntax in request body
6. Ensure database is running and accessible

### Common Issues:
- **Connection Refused**: Application not running
- **404 Not Found**: Wrong endpoint URL
- **400 Bad Request**: Check request body JSON syntax
- **500 Server Error**: Check application logs for stack trace
- **Foreign Key Error**: Referenced entity doesn't exist

---

## Test Coverage Checklist

### Functional Coverage
- ✅ All CRUD operations
- ✅ All business rules
- ✅ All workflows (happy path)
- ✅ Error scenarios
- ✅ Edge cases

### Non-Functional Coverage
- ⬜ Performance (load testing)
- ⬜ Security (penetration testing)
- ⬜ Usability (if frontend exists)
- ⬜ Compatibility (different environments)

---

## Next Steps After Manual Testing

1. **Document Bugs**: Create bug reports for failures
2. **Automation**: Convert to JUnit/RestAssured tests
3. **CI/CD Integration**: Add to build pipeline
4. **Regression Suite**: Maintain for future releases
5. **Performance Testing**: JMeter or Gatling
6. **Security Audit**: OWASP ZAP or similar

---

*Quick Reference Version: 1.0*  
*For detailed test scenarios, see TEST_SCENARIOS.md*
