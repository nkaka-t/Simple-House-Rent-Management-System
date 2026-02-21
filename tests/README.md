# Testing Documentation

Complete testing suite for the Simple House Rent Management System.

## 📋 Overview

This directory contains comprehensive testing documentation and resources for the **Spring Boot House Rent Management System** backend API.

**Important Note**: Your system is built with **Spring Boot (Java) + MySQL**, not React + Node.js + SQLite. All test scenarios are tailored for your actual technology stack.

---

## 📂 Files in This Directory

| File | Purpose | Lines |
|------|---------|-------|
| **TEST_SCENARIOS.md** | Complete manual test scenarios (100+ tests) | 1000+ |
| **QUICK_REFERENCE.md** | Quick test execution guide and cheat sheet | 300+ |
| **Postman_Collection.json** | Postman API test collection | 700+ |
| **AUTOMATED_TEST_TEMPLATES.md** | JUnit 5 test code templates | 600+ |
| **README.md** | This file - testing overview | - |

---

## 🎯 Test Coverage

### Test Categories

1. **Owner Management** (5 tests)
   - Create, retrieve owners
   - Validation scenarios

2. **House/Property Management** (10 tests)
   - CRUD operations
   - Tenant assignment
   - Status management

3. **Tenant Management** (8 tests)
   - Registration
   - House assignment
   - Leave operations

4. **Payment & Rent** (10 tests)
   - Payment generation
   - Payment processing
   - Debt calculations

5. **Business Rules** (7 tests)
   - Occupancy rules
   - Assignment constraints
   - Status updates

6. **API Endpoints** (5 tests)
   - HTTP methods
   - Response codes
   - Content types

7. **Database Operations** (7 tests)
   - Persistence
   - Transactions
   - Integrity

8. **Security & Validation** (8 tests)
   - Input validation
   - SQL injection prevention
   - XSS prevention

9. **Error Handling** (6 tests)
   - 404 responses
   - 400 validation errors
   - 500 server errors

10. **Edge Cases** (10+ tests)
    - Concurrency
    - Boundary values
    - Empty data

**Total**: 76+ comprehensive test scenarios

---

## 🚀 Quick Start

### Prerequisites

1. **Application Running**:
   ```bash
   cd c:\projects\Teta\simple-house-rent-management-system
   mvn spring-boot:run
   ```

2. **Database Running**:
   - MySQL 8.0 on localhost:3306
   - Database: `rent_management`

3. **Testing Tools**:
   - Postman (for manual API testing)
   - cURL (for command-line testing)
   - JUnit 5 (for automated testing)

---

## 📖 How to Use These Tests

### Option 1: Manual Testing with Postman

1. **Import Collection**:
   - Open Postman
   - Import `Postman_Collection.json`
   - Set baseUrl variable to `http://localhost:8080`

2. **Run Tests**:
   - Execute requests sequentially
   - Automated assertions verify responses
   - Collection Runner for batch execution

### Option 2: Manual Testing with cURL

1. **Open QUICK_REFERENCE.md**
2. **Copy cURL commands**
3. **Run in terminal**
4. **Verify responses**

### Option 3: Automated Testing with JUnit

1. **Copy templates from AUTOMATED_TEST_TEMPLATES.md**
2. **Create test classes in `src/test/java`**
3. **Run**: `mvn test`
4. **View coverage**: `mvn test jacoco:report`

---

## 📊 Test Execution Priority

### 🔴 Critical (Must Pass)
- Tenant assignment to house
- Payment generation
- Business rule enforcement
- Database transactions

### 🟡 High (Should Pass)
- All CRUD operations
- Validation rules
- Error handling

### 🟢 Medium (Nice to Have)
- Edge cases
- Performance testing

---

## 🛠️ Test Environment Setup

### Database Setup
```sql
CREATE DATABASE IF NOT EXISTS rent_management;
USE rent_management;

-- Clear test data
TRUNCATE TABLE payments;
TRUNCATE TABLE tenants;
TRUNCATE TABLE houses;
TRUNCATE TABLE owners;

-- Verify tables
SHOW TABLES;
```

### Verify Application
```bash
curl http://localhost:8080/owners
# Expected: [] or list of owners
```

---

## 📝 Test Execution Flow

### Complete Happy Path Test

```bash
# 1. Create Owner
curl -X POST http://localhost:8080/owners \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","phone":"+1234567890","email":"john@test.com"}'

# 2. Create House
curl -X POST http://localhost:8080/houses \
  -H "Content-Type: application/json" \
  -d '{"location":"123 Main St","rentAmount":1500,"ownerId":1}'

# 3. Create Tenant
curl -X POST http://localhost:8080/tenants \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Alice","phone":"+1555123","email":"alice@test.com","nationalId":"NID001","startDate":"2026-03-01"}'

# 4. Assign Tenant to House
curl -X PUT http://localhost:8080/houses/1/assign/1

# 5. Generate Payment
curl -X POST "http://localhost:8080/payments/generate/1?month=3&year=2026"

# 6. Mark as Paid
curl -X PUT http://localhost:8080/payments/1/pay

# 7. Check Debt
curl http://localhost:8080/payments/debt/1
```

---

## 🧪 Test Data Templates

### Valid Owner
```json
{
  "name": "Test Owner",
  "phone": "+1234567890",
  "email": "valid@example.com"
}
```

### Valid House
```json
{
  "location": "123 Test St",
  "rentAmount": 1500.00,
  "description": "2BR apt",
  "ownerId": 1
}
```

### Valid Tenant
```json
{
  "fullName": "Test Tenant",
  "phone": "+1987654321",
  "email": "tenant@example.com",
  "nationalId": "NID123",
  "startDate": "2026-03-01"
}
```

---

## 🐛 Common Issues & Solutions

### Issue: Connection Refused
**Solution**: Ensure application is running on port 8080

### Issue: 404 Not Found
**Solution**: Check endpoint URL and HTTP method

### Issue: 400 Bad Request
**Solution**: Validate JSON syntax and required fields

### Issue: Database Error
**Solution**: Verify MySQL is running and database exists

---

## 📈 Test Metrics

Track these metrics during testing:

- **Total Tests**: 76+
- **Pass Rate Target**: >95%
- **Critical Failures**: 0
- **High Priority Failures**: <3
- **Code Coverage**: >80% (for automated tests)

---

## 🔄 Test Cycle

1. **Smoke Tests**: Basic functionality (5-10 min)
2. **Functional Tests**: All features (30-60 min)
3. **Integration Tests**: End-to-end flows (20-30 min)
4. **Negative Tests**: Error handling (15-20 min)
5. **Edge Cases**: Boundary conditions (15-20 min)

**Total Estimated Time**: 1.5 - 2 hours for complete manual test execution

---

## 📚 Documentation References

- **TEST_SCENARIOS.md**: Detailed test cases with expected results
- **QUICK_REFERENCE.md**: Quick commands and tips
- **AUTOMATED_TEST_TEMPLATES.md**: JUnit code templates
- **API Documentation**: See main README.md

---

## 🎯 Next Steps

1. **Manual Testing**:
   - Import Postman collection
   - Execute test scenarios
   - Document findings

2. **Automation**:
   - Implement JUnit tests
   - Set up CI/CD pipeline
   - Configure code coverage

3. **Reporting**:
   - Log test results
   - Track bugs
   - Generate reports

---

## 💡 Tips for Effective Testing

1. **Test in Order**: Follow dependency chain (Owner → House → Tenant → Payment)
2. **Clean Data**: Reset database between test runs for consistency
3. **Document Issues**: Record failures with detailed steps
4. **Verify Twice**: Check both API response AND database state
5. **Edge Cases Matter**: Don't skip boundary condition tests

---

## 📞 Support

For questions about:
- **Test Scenarios**: Review TEST_SCENARIOS.md
- **Quick Commands**: Check QUICK_REFERENCE.md
- **Automation**: See AUTOMATED_TEST_TEMPLATES.md
- **API Details**: Refer to main README.md

---

## ✅ Test Completion Checklist

- [ ] All smoke tests pass
- [ ] CRUD operations verified for all entities
- [ ] Business rules enforced correctly
- [ ] Validation working as expected
- [ ] Error handling returns proper codes
- [ ] Edge cases handled gracefully
- [ ] Database operations are atomic
- [ ] API responses are consistent
- [ ] No critical bugs found
- [ ] Documentation updated

---

**Happy Testing! 🚀**

*Testing Documentation Version: 1.0*  
*Last Updated: February 21, 2026*  
*System: Spring Boot House Rent Management API*
