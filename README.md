# Simple House Rent Management System

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![Maven](https://img.shields.io/badge/Maven-3.6+-red)
![License](https://img.shields.io/badge/License-MIT-yellow)

A comprehensive Spring Boot REST API for managing house rentals, owners, tenants, and payment tracking.

## Technologies Used

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Data JPA**: For database operations
- **MySQL**: Database
- **Lombok**: To reduce boilerplate code
- **Maven**: Build and dependency management
- **Jakarta Validation**: For request validation

## Features

- **Owner Management**: Create and manage property owners
- **House Management**: Add houses, track availability, assign tenants
- **Tenant Management**: Register tenants and track their rental status
- **Payment Management**: Generate monthly rent, track payments, calculate debts
- **Business Rules Enforcement**: Automatic status updates, validation checks
- **Exception Handling**: Global exception handler with proper HTTP status codes
- **Data Validation**: Request validation with meaningful error messages

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Database Setup

1. Install MySQL and start the MySQL service

2. Create the database:
```sql
CREATE DATABASE rent_management;
```

3. Update database credentials in `src/main/resources/application.properties` if needed:
```properties
spring.datasource.username=root
spring.datasource.password=password
```

## Installation and Running

1. Clone or download the project

2. Navigate to the project directory:
```bash
cd simple-house-rent-management-system
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/simple-house-rent-management-system-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### Owner Endpoints

#### Create Owner
- **POST** `/owners`
- **Request Body**:
```json
{
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john@example.com"
}
```
- **Response**: `201 Created`
```json
{
  "id": 1,
  "name": "John Doe",
  "phone": "+1234567890",
  "email": "john@example.com"
}
```

#### Get All Owners
- **GET** `/owners`
- **Response**: `200 OK`
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "phone": "+1234567890",
    "email": "john@example.com"
  }
]
```

### House Endpoints

#### Create House
- **POST** `/houses`
- **Request Body**:
```json
{
  "location": "123 Main Street, Apt 4B",
  "rentAmount": 1500.00,
  "description": "2 bedroom apartment with parking",
  "ownerId": 1
}
```
- **Response**: `201 Created`
```json
{
  "id": 1,
  "location": "123 Main Street, Apt 4B",
  "rentAmount": 1500.00,
  "description": "2 bedroom apartment with parking",
  "status": "VACANT",
  "ownerName": "John Doe",
  "tenantName": null
}
```

#### Get All Houses
- **GET** `/houses`
- **Response**: `200 OK`

#### Assign Tenant to House
- **PUT** `/houses/{houseId}/assign/{tenantId}`
- **Response**: `200 OK`

#### Mark House as Vacant
- **PUT** `/houses/{houseId}/vacant`
- **Response**: `200 OK`

#### Get Vacant Houses
- **GET** `/houses/vacant`
- **Response**: `200 OK`

### Tenant Endpoints

#### Create Tenant
- **POST** `/tenants`
- **Request Body**:
```json
{
  "fullName": "Jane Smith",
  "phone": "+1987654321",
  "email": "jane@example.com",
  "nationalId": "AB123456",
  "startDate": "2026-01-01",
  "endDate": "2026-12-31"
}
```
- **Response**: `201 Created`
```json
{
  "id": 1,
  "fullName": "Jane Smith",
  "phone": "+1987654321",
  "email": "jane@example.com",
  "nationalId": "AB123456",
  "startDate": "2026-01-01",
  "endDate": "2026-12-31",
  "houseLocation": null
}
```

#### Get All Tenants
- **GET** `/tenants`
- **Response**: `200 OK`

#### Tenant Leaves House
- **PUT** `/tenants/{tenantId}/leave`
- **Response**: `200 OK`

### Payment Endpoints

#### Generate Monthly Rent
- **POST** `/payments/generate/{tenantId}?month=1&year=2026`
- **Query Parameters**:
  - `month`: Integer (1-12)
  - `year`: Integer (e.g., 2026)
- **Response**: `201 Created`
```json
{
  "id": 1,
  "month": 1,
  "year": 2026,
  "amount": 1500.00,
  "status": "UNPAID",
  "paymentDate": null,
  "tenantName": "Jane Smith",
  "houseLocation": "123 Main Street, Apt 4B"
}
```

#### Mark Payment as Paid
- **PUT** `/payments/{paymentId}/pay`
- **Response**: `200 OK`

#### Get Payments by Tenant
- **GET** `/payments/tenant/{tenantId}`
- **Response**: `200 OK`

#### Get Tenant Debt
- **GET** `/payments/debt/{tenantId}`
- **Response**: `200 OK`
```json
{
  "tenantId": 1,
  "tenantName": "Jane Smith",
  "totalDebt": 3000.00
}
```

#### Get Total Debt
- **GET** `/payments/debt/total`
- **Response**: `200 OK`
```json
5000.00
```

#### Get Monthly Rent Summary
- **GET** `/payments/monthly-summary?month=1&year=2026`
- **Response**: `200 OK`
```json
{
  "month": 1,
  "year": 2026,
  "totalExpected": 10000.00,
  "totalPaid": 7000.00,
  "totalUnpaid": 3000.00
}
```

## Business Rules

1. **Automatic Status Management**: When a tenant is assigned to a house, the house status automatically changes to `OCCUPIED`. When a tenant leaves, it changes to `VACANT`.

2. **Single Assignment**: A tenant cannot be assigned to more than one house at a time.

3. **Occupancy Validation**: A house cannot have more than one active tenant.

4. **Payment Generation**: Monthly rent payments are generated based on the house's rent amount.

5. **Debt Calculation**: Tenant debt is the sum of all `UNPAID` payments.

6. **Payment Tracking**: When a payment is marked as `PAID`, the payment date is automatically set to the current date.

## Error Handling

The API uses standardized error responses:

### Resource Not Found (404)
```json
{
  "timestamp": "2026-02-21T17:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Owner not found with id: 1",
  "path": "/owners/1"
}
```

### Business Logic Error (400)
```json
{
  "timestamp": "2026-02-21T17:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "House is already occupied",
  "path": "/houses/1/assign/2"
}
```

### Validation Error (400)
```json
{
  "timestamp": "2026-02-21T17:00:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "name": "Name is required",
    "email": "Email should be valid"
  },
  "path": "/owners"
}
```

## Project Structure

```
src/main/java/com/rentmanagement/
├── RentManagementApplication.java
├── controller/
│   ├── OwnerController.java
│   ├── HouseController.java
│   ├── TenantController.java
│   └── PaymentController.java
├── service/
│   ├── OwnerService.java
│   ├── HouseService.java
│   ├── TenantService.java
│   └── PaymentService.java
├── repository/
│   ├── OwnerRepository.java
│   ├── HouseRepository.java
│   ├── TenantRepository.java
│   └── PaymentRepository.java
├── entity/
│   ├── Owner.java
│   ├── House.java
│   ├── Tenant.java
│   └── Payment.java
├── dto/
│   ├── request/
│   │   ├── OwnerRequest.java
│   │   ├── HouseRequest.java
│   │   └── TenantRequest.java
│   └── response/
│       ├── OwnerResponse.java
│       ├── HouseResponse.java
│       ├── TenantResponse.java
│       ├── PaymentResponse.java
│       ├── DebtSummaryResponse.java
│       └── MonthlyRentSummaryResponse.java
├── enums/
│   ├── HouseStatus.java
│   └── PaymentStatus.java
└── exception/
    ├── ResourceNotFoundException.java
    ├── BusinessLogicException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java
```

## Testing the API

You can test the API using tools like:
- **Postman**: Import the endpoints and test each operation
- **cURL**: Command-line testing
- **Swagger/OpenAPI**: (Can be added with springdoc-openapi dependency)

### Example cURL Commands

Create an Owner:
```bash
curl -X POST http://localhost:8080/owners \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","phone":"+1234567890","email":"john@example.com"}'
```

Get All Houses:
```bash
curl -X GET http://localhost:8080/houses
```

## Development

### Building for Production

```bash
mvn clean package -DskipTests
```

The JAR file will be created in the `target/` directory.

### Database Schema

The application uses Hibernate to automatically create the database schema based on the entity classes. The schema includes:

- `owners` table
- `houses` table
- `tenants` table
- `payments` table

All tables are created automatically when you run the application for the first time.

## GitHub Workflow and Branching Strategy

This project follows a strict branch workflow for professional collaboration and code quality assurance.

### Branch Structure

- **`main`**: Production-ready stable code only. Direct commits are not allowed.
- **`qa`**: Quality assurance and testing branch. Code is tested here before merging to main.
- **`dev`**: Active development branch. All new features are developed here.

### Workflow Rules

1. **Development**: All new features and bug fixes must be developed in the `dev` branch or feature branches created from `dev`.
2. **Testing**: Once features are ready, merge `dev` → `qa` for testing and validation.
3. **Production**: Only tested and approved code from `qa` can be merged into `main`.
4. **No Direct Commits**: Never commit directly to `main` or `qa` branches.
5. **Pull Requests**: Always use Pull Requests for merging between branches.

### Branch Workflow Diagram

```
dev (feature development)
 │
 ├─→ Create feature branches
 │
 └─→ Merge to qa (after development complete)
      │
      ├─→ Run tests and QA validation
      │
      └─→ Merge to main (after testing complete)
           │
           └─→ Production deployment
```

### Commit Message Convention

We follow structured commit messages for clear project history:

**Format**: `<type>: <short description>`

**Types**:
- `feat`: New feature implementation
- `fix`: Bug fix
- `refactor`: Code refactoring without changing functionality
- `docs`: Documentation updates
- `chore`: Maintenance tasks, dependency updates
- `test`: Adding or updating tests
- `style`: Code formatting, missing semicolons, etc.

**Examples**:
```
feat: implement tenant assignment logic
fix: prevent assigning tenant to occupied house
refactor: improve payment service error handling
docs: update API endpoint documentation
chore: update Spring Boot version to 3.2.0
test: add unit tests for HouseService
```

## Contributing

We welcome contributions! Please read our [CONTRIBUTING.md](CONTRIBUTING.md) for detailed guidelines.

### Quick Contribution Steps

1. **Fork the repository**
2. **Clone your fork**:
   ```bash
   git clone https://github.com/your-username/simple-house-rent-management-system.git
   ```
3. **Create a feature branch from dev**:
   ```bash
   git checkout dev
   git checkout -b feature/your-feature-name
   ```
4. **Make your changes and commit**:
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   ```
5. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```
6. **Create a Pull Request** to the `dev` branch

### Code Quality Standards

- Follow Java naming conventions
- Write meaningful variable and method names
- Add JavaDoc comments for public methods
- Ensure all validations are in place
- Write unit tests for new features
- Maintain the existing code structure
- Follow SOLID principles

## License

This project is open source and available for educational purposes.

## Support

For issues or questions, please create an issue in the project repository.
