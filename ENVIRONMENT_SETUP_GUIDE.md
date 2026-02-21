# Environment Setup Guide
**Complete Setup Guide for Spring Boot Development**

---

## 🎯 Current Environment Status

✅ **Java**: Installed (Java 24.0.2)  
❌ **Maven**: Not Installed  
⚠️ **MySQL**: Not Found/Not Running  

⚠️ **Critical Issue**: Project requires **Java 17**, but Java 24 is installed

---

## 📋 Prerequisites Installation

### 1. Java 17 Installation (CRITICAL)

Your project uses **Spring Boot 3.2.0** which requires **Java 17**.  
You currently have **Java 24**, which may cause compatibility issues.

#### Option A: Install Java 17 Alongside Java 24 (Recommended)

```powershell
# Download Java 17 from Adoptium
# Go to: https://adoptium.net/temurin/releases/?version=17

# OR use Chocolatey
choco install temurin17

# After installation, set JAVA_HOME for this project only
# Create setenv.ps1 in project root:
```

**Create file**: `setenv.ps1`
```powershell
# Set Java 17 for this project
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Java 17 activated for this project"
java -version
```

**Usage**:
```powershell
# Run this before starting the project
.\setenv.ps1
```

#### Option B: Replace Java 24 with Java 17

```powershell
# Uninstall Java 24 (if needed)
# Control Panel > Programs > Uninstall

# Install Java 17
choco install temurin17

# OR download from: https://adoptium.net/
```

#### Verify Java Installation

```powershell
# Check Java version
java -version
# Should show: openjdk version "17.0.x"

# Check JAVA_HOME
echo $env:JAVA_HOME
# Should point to Java 17 directory

# If not set, add to environment variables
[System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot', 'User')
```

---

### 2. Maven Installation (REQUIRED)

#### Option A: Using Chocolatey (Easiest)

```powershell
# Install Chocolatey (if not installed)
Set-ExecutionPolicy Bypass -Scope Process -Force
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install Maven
choco install maven

# Verify
mvn --version
```

#### Option B: Manual Installation

1. **Download Maven**:
   - Go to: https://maven.apache.org/download.cgi
   - Download `apache-maven-3.9.x-bin.zip`

2. **Extract**:
   ```powershell
   # Extract to C:\apache-maven-3.9.x
   Expand-Archive -Path "C:\Users\YourName\Downloads\apache-maven-3.9.x-bin.zip" -DestinationPath "C:\"
   ```

3. **Add to PATH**:
   ```powershell
   # Add Maven to PATH
   $mavenPath = "C:\apache-maven-3.9.x\bin"
   [System.Environment]::SetEnvironmentVariable('PATH', "$env:PATH;$mavenPath", 'User')
   
   # Set M2_HOME
   [System.Environment]::SetEnvironmentVariable('M2_HOME', 'C:\apache-maven-3.9.x', 'User')
   ```

4. **Restart Terminal** and verify:
   ```powershell
   mvn --version
   ```

**Expected Output**:
```
Apache Maven 3.9.x
Maven home: C:\apache-maven-3.9.x
Java version: 17.0.x, vendor: Eclipse Adoptium
```

---

### 3. MySQL Installation (REQUIRED)

#### Option A: Using Chocolatey

```powershell
# Install MySQL
choco install mysql

# Start MySQL service
net start MySQL

# Set root password (if not set)
mysql -u root -p
# (Press Enter if no password, then set one)
```

#### Option B: Manual Installation

1. **Download MySQL**:
   - Go to: https://dev.mysql.com/downloads/mysql/
   - Download MySQL Installer (Windows)

2. **Install**:
   - Run installer
   - Choose "Developer Default"
   - Set root password: `password` (or any password)
   - Complete installation

3. **Start Service**:
   ```powershell
   # Start MySQL service
   net start MySQL

   # OR use Services.msc to start MySQL service
   ```

#### Option C: Using Docker (Alternative)

```powershell
# Install Docker Desktop
choco install docker-desktop

# Pull MySQL image
docker pull mysql:8.0

# Run MySQL container
docker run --name mysql-rent-system `
  -e MYSQL_ROOT_PASSWORD=password `
  -e MYSQL_DATABASE=rent_management `
  -p 3306:3306 `
  -d mysql:8.0

# Verify
docker ps
```

#### Verify MySQL Installation

```powershell
# Check service status
Get-Service MySQL* | Select-Object Name, Status

# Connect to MySQL
mysql -u root -p
# Enter password: password

# Test connection
mysql -u root -p -e "SELECT VERSION();"
```

---

## 🔧 Environment Setup Script

I've created an automated setup verification script:

**Create file**: `setup-environment.ps1`

```powershell
# Environment Setup and Verification Script
# Simple House Rent Management System

Write-Host "=== Environment Setup Verification ===" -ForegroundColor Cyan
Write-Host ""

# Function to check command exists
function Test-Command {
    param($Command)
    $null = Get-Command $Command -ErrorAction SilentlyContinue
    return $?
}

# Check Java
Write-Host "1. Checking Java..." -ForegroundColor Yellow
if (Test-Command java) {
    $javaVersion = java -version 2>&1 | Select-String -Pattern 'version' | Select-Object -First 1
    Write-Host "   ✓ Java found: $javaVersion" -ForegroundColor Green
    
    # Check if Java 17
    if ($javaVersion -match '"17\.') {
        Write-Host "   ✓ Java 17 detected - Compatible!" -ForegroundColor Green
    } elseif ($javaVersion -match '"24\.') {
        Write-Host "   ⚠ Java 24 detected - Project requires Java 17!" -ForegroundColor Red
        Write-Host "   Action: Install Java 17 from https://adoptium.net/" -ForegroundColor Yellow
    } else {
        Write-Host "   ⚠ Non-standard Java version detected" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ✗ Java not found!" -ForegroundColor Red
    Write-Host "   Action: Install Java 17 from https://adoptium.net/" -ForegroundColor Yellow
}
Write-Host ""

# Check Maven
Write-Host "2. Checking Maven..." -ForegroundColor Yellow
if (Test-Command mvn) {
    $mavenVersion = mvn --version 2>&1 | Select-String -Pattern 'Apache Maven' | Select-Object -First 1
    Write-Host "   ✓ Maven found: $mavenVersion" -ForegroundColor Green
} else {
    Write-Host "   ✗ Maven not found!" -ForegroundColor Red
    Write-Host "   Action: Run 'choco install maven' or download from https://maven.apache.org/" -ForegroundColor Yellow
}
Write-Host ""

# Check MySQL
Write-Host "3. Checking MySQL..." -ForegroundColor Yellow
$mysqlService = Get-Service -Name MySQL* -ErrorAction SilentlyContinue
if ($mysqlService) {
    Write-Host "   ✓ MySQL service found: $($mysqlService.Name)" -ForegroundColor Green
    if ($mysqlService.Status -eq 'Running') {
        Write-Host "   ✓ MySQL is running" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ MySQL is not running. Status: $($mysqlService.Status)" -ForegroundColor Yellow
        Write-Host "   Action: Run 'net start $($mysqlService.Name)'" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ✗ MySQL not found!" -ForegroundColor Red
    Write-Host "   Action: Run 'choco install mysql' or download from https://dev.mysql.com/" -ForegroundColor Yellow
}
Write-Host ""

# Summary
Write-Host "=== Summary ===" -ForegroundColor Cyan
$readyToBuild = (Test-Command java) -and (Test-Command mvn) -and ($mysqlService -ne $null)

if ($readyToBuild) {
    Write-Host "✓ Environment is ready for development!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Yellow
    Write-Host "1. Start MySQL: net start MySQL" -ForegroundColor White
    Write-Host "2. Create database: mysql -u root -p -e 'CREATE DATABASE rent_management;'" -ForegroundColor White
    Write-Host "3. Build project: mvn clean compile" -ForegroundColor White
    Write-Host "4. Run application: mvn spring-boot:run" -ForegroundColor White
} else {
    Write-Host "✗ Environment setup incomplete" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install missing components and run this script again." -ForegroundColor Yellow
}
Write-Host ""
```

**Run the script**:
```powershell
.\setup-environment.ps1
```

---

## 🗄️ Database Setup

### Step 1: Start MySQL Service

```powershell
# Start MySQL
net start MySQL

# OR if using Docker
docker start mysql-rent-system
```

### Step 2: Create Database

```powershell
# Connect to MySQL
mysql -u root -p
# Enter password: password
```

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS rent_management;

-- Verify
SHOW DATABASES;

-- Use database
USE rent_management;

-- Exit
EXIT;
```

### Step 3: Verify Connection from Command Line

```powershell
mysql -u root -p rent_management -e "SELECT DATABASE();"
```

### Step 4: Update application.properties (if needed)

Check file: `src/main/resources/application.properties`

```properties
# Should match your MySQL credentials
spring.datasource.url=jdbc:mysql://localhost:3306/rent_management
spring.datasource.username=root
spring.datasource.password=password
```

---

## 🏗️ Build and Run the Application

### Step 1: Verify Environment

```powershell
# Run verification script
.\setup-environment.ps1
```

### Step 2: Clean and Compile

```powershell
# Navigate to project directory
cd C:\projects\Teta\simple-house-rent-management-system

# Clean and compile
mvn clean compile
```

**Expected Output**:
```
[INFO] BUILD SUCCESS
[INFO] Total time: 15.234 s
```

### Step 3: Run Tests (Optional)

```powershell
# Run unit tests (when implemented)
mvn test
```

### Step 4: Package Application

```powershell
# Create JAR file
mvn clean package

# JAR will be created in: target/simple-house-rent-management-system-1.0.0.jar
```

### Step 5: Run Application

#### Option A: Using Maven

```powershell
mvn spring-boot:run
```

#### Option B: Using JAR

```powershell
java -jar target/simple-house-rent-management-system-1.0.0.jar
```

#### Option C: Using IDE
1. Open project in IntelliJ IDEA or Eclipse
2. Find `RentManagementApplication.java`
3. Right-click > Run

**Expected Output**:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

...
Started RentManagementApplication in 5.234 seconds
```

**Application Running**: http://localhost:8080

---

## ✅ Verification Checklist

### Environment Setup
- [ ] Java 17 installed and in PATH
- [ ] Maven installed and in PATH
- [ ] MySQL installed and service running
- [ ] Database `rent_management` created
- [ ] `setup-environment.ps1` script passes all checks

### Build Verification
- [ ] `mvn clean compile` succeeds
- [ ] No compilation errors
- [ ] All dependencies downloaded

### Application Startup
- [ ] Application starts without errors
- [ ] Port 8080 is accessible
- [ ] Database connection established
- [ ] Tables auto-created by Hibernate

### Basic Functionality
- [ ] Can access http://localhost:8080/owners
- [ ] Can create owner via POST request
- [ ] No errors in console logs

---

## 🚨 Common Issues and Solutions

### Issue 1: Java Version Mismatch

**Error**: `Unsupported class file major version XX`

**Solution**:
```powershell
# Install Java 17
choco install temurin17

# Set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot"

# Verify
java -version
```

### Issue 2: Maven Not Found

**Error**: `mvn : The term 'mvn' is not recognized`

**Solution**:
```powershell
# Install Maven
choco install maven

# Restart terminal
# Verify
mvn --version
```

### Issue 3: MySQL Connection Refused

**Error**: `Communications link failure`

**Solution**:
```powershell
# Start MySQL service
net start MySQL

# Check service
Get-Service MySQL*

# Test connection
mysql -u root -p -e "SELECT 1;"
```

### Issue 4: Port 8080 Already in Use

**Error**: `Port 8080 was already in use`

**Solution**:
```powershell
# Find process using port 8080
netstat -ano | findstr :8080

# Kill process (replace PID)
taskkill /PID <PID> /F

# OR change port in application.properties
# server.port=8081
```

### Issue 5: Database Not Created

**Error**: `Unknown database 'rent_management'`

**Solution**:
```powershell
mysql -u root -p -e "CREATE DATABASE rent_management;"
```

### Issue 6: Lombok Not Working

**Error**: `Cannot find symbol` for getters/setters

**Solution**:
```powershell
# Rebuild with clean
mvn clean install

# If using IDE, install Lombok plugin
# IntelliJ: Settings > Plugins > Search "Lombok"
```

---

## 📊 Environment Status Matrix

| Component | Required | Current | Status | Action |
|-----------|----------|---------|--------|--------|
| Java 17 | Yes | Java 24 | ❌ | Install Java 17 |
| Maven 3.6+ | Yes | Not Installed | ❌ | Install Maven |
| MySQL 8.0 | Yes | Unknown | ⚠️ | Install/Start MySQL |
| Git | Yes | Installed | ✅ | None |
| IDE (Optional) | No | - | - | Install if needed |

---

## 🎯 Quick Setup Commands

**Complete setup in 5 commands**:

```powershell
# 1. Install Java 17
choco install temurin17

# 2. Install Maven
choco install maven

# 3. Install MySQL
choco install mysql

# 4. Start MySQL and create database
net start MySQL
mysql -u root -p -e "CREATE DATABASE rent_management;"

# 5. Build and run
cd C:\projects\Teta\simple-house-rent-management-system
mvn clean compile
mvn spring-boot:run
```

---

## 📞 Need Help?

If you encounter issues:

1. Run `.\setup-environment.ps1` to diagnose
2. Check logs in console output
3. Verify all components installed correctly
4. Check `application.properties` for correct settings
5. Review error messages carefully

---

## 🎓 Next Steps After Setup

1. ✅ Complete environment setup
2. ✅ Build project successfully
3. ✅ Start application
4. ⏭️ Run smoke tests
5. ⏭️ Execute full test suite
6. ⏭️ Push to GitHub (if not done)
7. ⏭️ Document results

---

**Last Updated**: February 21, 2026  
**Script Version**: 1.0  
**Estimated Setup Time**: 30-45 minutes

---

*Run `.\setup-environment.ps1` to verify your environment status!*
