# Contributing to Simple House Rent Management System

Thank you for your interest in contributing to our project! We appreciate your time and effort to help improve this application.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Branch Workflow](#branch-workflow)
- [Development Guidelines](#development-guidelines)
- [Commit Message Guidelines](#commit-message-guidelines)
- [Pull Request Process](#pull-request-process)
- [Coding Standards](#coding-standards)
- [Testing Requirements](#testing-requirements)

## Code of Conduct

This project adheres to a Code of Conduct that all contributors are expected to follow. Please read [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) before contributing.

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:
- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Git
- Your favorite IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Setting Up Your Development Environment

1. **Fork the repository** on GitHub

2. **Clone your fork** to your local machine:
   ```bash
   git clone https://github.com/your-username/simple-house-rent-management-system.git
   cd simple-house-rent-management-system
   ```

3. **Add the upstream repository**:
   ```bash
   git remote add upstream https://github.com/original-owner/simple-house-rent-management-system.git
   ```

4. **Set up the database**:
   ```sql
   CREATE DATABASE rent_management;
   ```

5. **Update application.properties** with your MySQL credentials

6. **Build the project**:
   ```bash
   mvn clean install
   ```

7. **Run the application** to verify everything works:
   ```bash
   mvn spring-boot:run
   ```

## Branch Workflow

We follow a strict three-branch workflow:

### Branch Structure

- **`main`**: Production-ready code only. This branch should always be stable and deployable.
- **`qa`**: Quality assurance branch. Code is tested here before going to production.
- **`dev`**: Active development branch. All new features start here.

### Workflow Steps

1. **Always work on the `dev` branch or create feature branches from `dev`**:
   ```bash
   git checkout dev
   git pull upstream dev
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** in your feature branch

3. **Keep your branch updated** with the latest `dev` changes:
   ```bash
   git checkout dev
   git pull upstream dev
   git checkout feature/your-feature-name
   git merge dev
   ```

4. **Submit a Pull Request** to merge your feature branch into `dev`

### Branch Naming Conventions

- Feature branches: `feature/short-description`
- Bug fix branches: `fix/short-description`
- Hotfix branches: `hotfix/short-description`
- Refactor branches: `refactor/short-description`

**Examples**:
- `feature/add-payment-history`
- `fix/tenant-assignment-validation`
- `refactor/improve-service-layer`

## Development Guidelines

### Project Structure

Maintain the existing layered architecture:
```
controller → service → repository → entity
```

### Adding New Features

When adding a new feature:

1. **Entity Layer**: Create or update entity classes if needed
2. **Repository Layer**: Add repository interfaces with custom queries
3. **DTO Layer**: Create request and response DTOs
4. **Service Layer**: Implement business logic
5. **Controller Layer**: Create REST endpoints
6. **Exception Handling**: Add custom exceptions if needed
7. **Validation**: Add proper validation annotations
8. **Documentation**: Update README if API changes

### Code Organization

- Keep related code together
- Follow the Single Responsibility Principle
- Avoid code duplication
- Use meaningful names for classes, methods, and variables
- Keep methods small and focused

## Commit Message Guidelines

We follow a structured commit message format for clarity and consistency.

### Format

```
<type>: <short description>

[optional body]

[optional footer]
```

### Types

- **feat**: A new feature
- **fix**: A bug fix
- **refactor**: Code change that neither fixes a bug nor adds a feature
- **docs**: Documentation only changes
- **style**: Changes that don't affect code meaning (formatting, missing semicolons)
- **test**: Adding or updating tests
- **chore**: Maintenance tasks, dependency updates, build changes

### Examples

```
feat: add endpoint to retrieve payment history by date range

Added new endpoint GET /payments/history that accepts startDate and endDate
parameters to filter payment records.

Closes #42
```

```
fix: prevent duplicate tenant assignments

Added validation check in HouseService to ensure a tenant can only be
assigned to one house at a time.

Fixes #38
```

```
refactor: optimize debt calculation query

Improved PaymentService.calculateTotalDebt() to use database aggregation
instead of loading all records into memory.
```

### Commit Message Rules

- Use the imperative mood ("add feature" not "added feature")
- Keep the first line under 50 characters
- Capitalize the first letter of the description
- Don't end the description with a period
- Use the body to explain what and why, not how
- Reference issue numbers when applicable

## Pull Request Process

### Before Submitting a PR

1. **Ensure your code builds successfully**:
   ```bash
   mvn clean install
   ```

2. **Run all tests** (when available):
   ```bash
   mvn test
   ```

3. **Check for compilation errors and warnings**

4. **Update documentation** if you've changed APIs or added features

5. **Ensure your code follows the coding standards** (see below)

### Submitting a Pull Request

1. **Push your changes** to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```

2. **Go to GitHub** and create a Pull Request from your feature branch to the `dev` branch of the upstream repository

3. **Fill out the PR template** completely (see `.github/PULL_REQUEST_TEMPLATE.md`)

4. **Wait for review**:
   - Address any feedback from reviewers
   - Make requested changes in new commits
   - Keep the discussion professional and constructive

5. **Once approved**, a maintainer will merge your PR

### Pull Request Guidelines

- One feature per PR (keep PRs focused and small)
- Include a clear description of what the PR does
- Reference any related issues
- Add screenshots for UI changes (if applicable)
- Ensure CI/CD checks pass (when configured)
- Be responsive to feedback and questions

## Coding Standards

### Java Code Style

- **Indentation**: 4 spaces (no tabs)
- **Line length**: Maximum 120 characters
- **Braces**: Always use braces, even for single-line if statements
- **Naming conventions**:
  - Classes: PascalCase (e.g., `HouseService`)
  - Methods: camelCase (e.g., `createTenant`)
  - Variables: camelCase (e.g., `tenantId`)
  - Constants: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`)

### Spring Boot Best Practices

- Use constructor injection with `@RequiredArgsConstructor` from Lombok
- Keep controllers thin, put logic in services
- Use DTOs for request/response objects
- Add `@Transactional` on service methods that modify data
- Use proper HTTP status codes in responses
- Validate all inputs with Jakarta Validation annotations

### Exception Handling

- Use custom exceptions (`ResourceNotFoundException`, `BusinessLogicException`)
- Let `GlobalExceptionHandler` handle exceptions
- Provide meaningful error messages
- Don't catch generic `Exception` unless necessary

### Documentation

- Add JavaDoc comments for public methods
- Include parameter descriptions
- Document return values
- Explain complex logic with inline comments
- Update README for API changes

### Example Code Style

```java
@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final HouseRepository houseRepository;

    /**
     * Creates a new tenant in the system.
     *
     * @param request the tenant creation request containing tenant details
     * @return TenantResponse with created tenant information
     */
    @Transactional
    public TenantResponse createTenant(TenantRequest request) {
        Tenant tenant = new Tenant();
        tenant.setFullName(request.getFullName());
        tenant.setPhone(request.getPhone());
        tenant.setEmail(request.getEmail());
        
        Tenant savedTenant = tenantRepository.save(tenant);
        return convertToResponse(savedTenant);
    }
}
```

## Testing Requirements

### Writing Tests

- Write unit tests for all service layer methods
- Test both success and failure scenarios
- Use meaningful test method names
- Follow the Arrange-Act-Assert pattern
- Mock dependencies appropriately

### Test Coverage

- Aim for at least 80% code coverage for new code
- All business logic must be tested
- Critical paths must have comprehensive tests

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=HouseServiceTest

# Run with coverage report
mvn test jacoco:report
```

## Questions or Need Help?

- Open an issue for bugs or feature requests
- Tag maintainers for urgent questions
- Join our community discussions (if applicable)

## Recognition

Contributors who make significant contributions will be recognized in our project documentation.

Thank you for contributing to Simple House Rent Management System! 🎉
