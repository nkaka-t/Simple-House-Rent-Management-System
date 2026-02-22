# House Rent Management System - Postman Testing Guide

## 🚀 Setup Instructions

### 1. Import Files into Postman

1. **Import Environment:**
   - Open Postman
   - Click "Import" button
   - Select `Postman_Environment.json`
   - This creates environment variables for testing

2. **Import Collection:**
   - Click "Import" button
   - Select `Postman_Complete_Collection.json`
   - This creates all API test requests

3. **Set Environment:**
   - Click environment dropdown (top right)
   - Select "House Rent Management System - Test Environment"

### 2. Test Data Already Created

The following test data has been pre-loaded in your system:

**Owners:**
- ID 1: John Smith (john.smith@example.com)
- ID 2: Alice Johnson (alice.johnson@example.com)
- ID 3: Alice Johnson (duplicate from earlier test)

**Houses:**
- ID 1: 123 Main Street, Downtown (Vacant, Owner ID 2)
- ID 2: 456 Oak Avenue, Suburbs (Occupied, Owner ID 3)

**Tenants:**
- ID 1: Robert Wilson (NID123456789, House ID 2)

### 3. Testing Workflow

#### Phase 1: Basic CRUD Operations
1. Run "Get All Owners" to verify existing data
2. Run "Create Owner - John Smith" and "Create Owner - Alice Johnson"
3. Run "Get Owner by ID" with different IDs
4. Run "Update Owner" to modify existing owner

#### Phase 2: House Management
1. Run "Get All Houses" to see existing houses
2. Create new houses using the house creation requests
3. Test house retrieval and updates

#### Phase 3: Tenant Management
1. Run "Get All Tenants" to see existing tenant
2. Create new tenants with valid house IDs
3. Test tenant retrieval and updates

#### Phase 4: Payment Management
1. Run payment creation requests
2. Test payment retrieval and updates
3. Note: Payment creation may have issues - see troubleshooting below

#### Phase 5: Error Testing
1. Run invalid data tests to verify validation
2. Test non-existent resource access
3. Verify proper error responses

## 🛠️ Troubleshooting

### Payment Creation Issues
If you encounter 500 errors with payment creation:
1. Ensure tenant and house IDs exist
2. Check that the tenant is assigned to the house
3. Verify date formats are correct (YYYY-MM-DD)
4. Try with minimal data first:
   ```json
   {
     "amount": 100.00,
     "month": 1,
     "year": 2026,
     "status": "UNPAID",
     "tenantId": 1,
     "houseId": 2
   }
   ```

### Common Issues and Solutions

1. **404 Not Found:**
   - Verify the resource ID exists
   - Check if the resource was deleted

2. **400 Bad Request:**
   - Check JSON format
   - Verify required fields are present
   - Ensure data types are correct

3. **500 Internal Server Error:**
   - Check application logs
   - Verify database connectivity
   - Ensure foreign key relationships are valid

## 📋 Testing Checklist

### Owner Tests ✅
- [ ] Create owner with valid data
- [ ] Retrieve all owners
- [ ] Retrieve owner by ID
- [ ] Update owner information
- [ ] Handle duplicate emails
- [ ] Validate required fields

### House Tests ✅
- [ ] Create house with valid owner
- [ ] Retrieve all houses
- [ ] Retrieve house by ID
- [ ] Update house information
- [ ] Validate rent amount (positive)
- [ ] Check owner-house relationship

### Tenant Tests ✅
- [ ] Create tenant with valid house
- [ ] Retrieve all tenants
- [ ] Retrieve tenant by ID
- [ ] Update tenant information
- [ ] Validate national ID uniqueness
- [ ] Check date validation (start < end)

### Payment Tests ⚠️
- [ ] Create payment with valid tenant/house
- [ ] Retrieve all payments
- [ ] Retrieve payment by ID
- [ ] Update payment status
- [ ] Validate amount (positive)
- [ ] Check month/year validation

### Error Handling Tests ✅
- [ ] Invalid data formats
- [ ] Missing required fields
- [ ] Non-existent resource access
- [ ] Duplicate unique constraints
- [ ] Invalid foreign key references

## 🎯 Expected Results

### Success Responses:
- **200 OK**: Successful GET/PUT requests
- **201 Created**: Successful POST requests
- **204 No Content**: Successful DELETE requests

### Error Responses:
- **400 Bad Request**: Validation errors
- **404 Not Found**: Resource doesn't exist
- **409 Conflict**: Duplicate constraints
- **500 Internal Server Error**: Server issues

## 🔄 Test Execution Order

1. **Setup Phase**: Import files and verify environment
2. **Data Creation**: Run all CREATE requests to build test data
3. **Data Retrieval**: Run GET requests to verify data creation
4. **Data Modification**: Run UPDATE requests to test modifications
5. **Error Testing**: Run invalid requests to test error handling
6. **Cleanup**: Optionally delete test data if needed

## 📊 Monitoring

Check the application console for:
- Hibernate SQL queries
- Error stack traces
- Request processing logs
- Database connection status

The system is now ready for comprehensive Postman testing!