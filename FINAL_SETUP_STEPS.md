# Final Setup Steps - Complete Environment Configuration

## 📋 Status Summary

✅ **Project Code**: Complete (32 Java files, 16 REST endpoints)  
✅ **Documentation**: Complete (4,560+ lines)  
✅ **Git Workflow**: Complete (dev, qa, main branches)  
✅ **GitHub Push**: Complete (based on git history)  
❌ **Maven**: Not installed  
❌ **Java 17**: Need to install alongside Java 24  
❌ **MySQL**: Not installed or running  
⏳ **Build & Test**: Pending environment setup  

## 🎯 Required Actions to Complete Project

### Step 1: Install Java 17 (CRITICAL)

Your project requires **Java 17** but you have **Java 24**. This is critical for Spring Boot 3.2.0 compatibility.

**Option A: Install Java 17 with Chocolatey**
```powershell
# If you have Chocolatey:
choco install temurin17

# Set JAVA_HOME for this project:
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

**Option B: Manual Installation**
1. Download from: https://adoptium.net/temurin/releases/?version=17
2. Install to default location
3. Set environment variables

### Step 2: Install Maven (CRITICAL)

**Option A: With Chocolatey**
```powershell
choco install maven
```

**Option B: Manual Installation**
1. Download from: https://maven.apache.org/download.cgi
2. Extract to `C:\apache-maven-3.9.x`
3. Add to PATH: `C:\apache-maven-3.9.x\bin`

### Step 3: Install MySQL (CRITICAL)

**Option A: With Chocolatey**
```powershell
choco install mysql
net start MySQL
```

**Option B: Manual Installation**
1. Download from: https://dev.mysql.com/downloads/mysql/
2. Install with default settings
3. Start service: `net start MySQL`

### Step 4: Create Database
```powershell
mysql -u root -p -e "CREATE DATABASE rent_management;"
```

## 🏗️ Build and Run Commands

Once environment is ready:

```powershell
# Navigate to project
cd C:\projects\Teta\simple-house-rent-management-system

# Verify Java version
java -version  # Should show 17.x.x

# Verify Maven
mvn --version

# Clean and compile
mvn clean compile

# Package
mvn clean package

# Run application
mvn spring-boot:run
```

## 🧪 Testing Commands

After application starts:

```powershell
# Test endpoints
curl http://localhost:8080/owners
curl http://localhost:8080/houses
curl http://localhost:8080/tenants
curl http://localhost:8080/payments

# Create a test owner
curl -X POST http://localhost:8080/owners `
  -H "Content-Type: application/json" `
  -d "{\"name\":\"Test Owner\",\"phone\":\"+1234567890\",\"email\":\"test@example.com\"}"
```

## ✅ Verification Checklist

### Environment Setup
- [ ] Java 17 installed and in PATH
- [ ] Maven installed and in PATH  
- [ ] MySQL installed and running
- [ ] Database `rent_management` created
- [ ] `setup-environment.ps1` shows all green checks

### Build Process
- [ ] `mvn clean compile` succeeds
- [ ] No compilation errors
- [ ] All dependencies resolved

### Application Startup
- [ ] `mvn spring-boot:run` starts successfully
- [ ] Application accessible at http://localhost:8080
- [ ] Database tables created automatically
- [ ] No startup errors

### API Functionality
- [ ] GET /owners returns empty array `[]`
- [ ] POST /owners creates new owner
- [ ] All 16 endpoints respond correctly
- [ ] Business logic works (tenant assignment, payment generation)

## 🚀 Quick Start Script

Run this once environment is ready:

```powershell
# Quick build and start
cd C:\projects\Teta\simple-house-rent-management-system
mvn clean compile && mvn spring-boot:run
```

## 📊 Expected Application Startup Output

Look for this success message:
```
Started RentManagementApplication in X.XXX seconds
```

Then verify at: http://localhost:8080/owners

## 🛠️ Troubleshooting

### Common Issues:
1. **Wrong Java version**: Ensure Java 17 is used
2. **Maven not found**: Check PATH and restart terminal
3. **MySQL connection**: Verify service is running and credentials
4. **Port already in use**: Kill existing process on port 8080

### Commands to Diagnose:
```powershell
# Check Java
java -version

# Check Maven
mvn --version

# Check MySQL
Get-Service MySQL*

# Check port
netstat -ano | findstr :8080
```

## 🎯 Next Steps After Environment Setup

1. **Build**: `mvn clean compile`
2. **Run**: `mvn spring-boot:run`
3. **Test**: Execute API calls
4. **Document**: Record test results
5. **Finalize**: Complete project handoff

---

## 🏁 Project Completion Criteria

✅ **Environment**: Java 17, Maven, MySQL installed  
✅ **Build**: Project compiles successfully  
✅ **Run**: Application starts without errors  
✅ **Test**: Basic API functionality verified  
✅ **Documentation**: All steps recorded  

**Estimated Time**: 30-60 minutes after environment installation

---

**Last Updated**: February 21, 2026  
**Project**: Simple House Rent Management System  
**Status**: Environment Setup Required

---

*Once you complete the environment setup, run `.\setup-environment.ps1` to verify all components are ready.*