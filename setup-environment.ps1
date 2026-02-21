# Environment Setup and Verification Script
# Simple House Rent Management System

Write-Host "==================================================================" -ForegroundColor Cyan
Write-Host "   Simple House Rent Management System - Environment Setup" -ForegroundColor Cyan
Write-Host "==================================================================" -ForegroundColor Cyan
Write-Host ""

# Function to check command exists
function Test-Command {
    param($Command)
    $null = Get-Command $Command -ErrorAction SilentlyContinue
    return $?
}

# Check Java
Write-Host "1. Checking Java Installation..." -ForegroundColor Yellow
Write-Host "   ----------------------------------------" -ForegroundColor DarkGray
if (Test-Command java) {
    try {
        $javaVersionOutput = java -version 2>&1 | Out-String
        $javaVersion = ($javaVersionOutput -split "`n")[0]
        Write-Host "   ✓ Java found: $javaVersion" -ForegroundColor Green
        
        # Extract version number
        if ($javaVersionOutput -match '"17\.') {
            Write-Host "   ✓ Java 17 detected - Perfect!" -ForegroundColor Green
            $javaOk = $true
        } elseif ($javaVersionOutput -match '"24\.') {
            Write-Host "   ⚠ WARNING: Java 24 detected!" -ForegroundColor Red
            Write-Host "   ⚠ This project requires Java 17!" -ForegroundColor Red
            Write-Host "   → Install Java 17: choco install temurin17" -ForegroundColor Yellow
            $javaOk = $false
        } elseif ($javaVersionOutput -match '"(\d+)\.') {
            $detectedVersion = $matches[1]
            Write-Host "   ⚠ Java $detectedVersion detected - Project requires Java 17" -ForegroundColor Yellow
            $javaOk = $false
        } else {
            Write-Host "   ⚠ Unknown Java version" -ForegroundColor Yellow
            $javaOk = $false
        }
        
        # Check JAVA_HOME
        if ($env:JAVA_HOME) {
            Write-Host "   ✓ JAVA_HOME set: $env:JAVA_HOME" -ForegroundColor Green
        } else {
            Write-Host "   ⚠ JAVA_HOME not set" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "   ✗ Error checking Java version" -ForegroundColor Red
        $javaOk = $false
    }
} else {
    Write-Host "   ✗ Java not found in PATH!" -ForegroundColor Red
    Write-Host "   → Install Java 17: choco install temurin17" -ForegroundColor Yellow
    Write-Host "   → Or download from: https://adoptium.net/temurin/releases/?version=17" -ForegroundColor Yellow
    $javaOk = $false
}
Write-Host ""

# Check Maven
Write-Host "2. Checking Maven Installation..." -ForegroundColor Yellow
Write-Host "   ----------------------------------------" -ForegroundColor DarkGray
if (Test-Command mvn) {
    try {
        $mavenVersionOutput = mvn --version 2>&1 | Out-String
        $mavenVersion = ($mavenVersionOutput -split "`n")[0]
        Write-Host "   ✓ Maven found: $mavenVersion" -ForegroundColor Green
        
        # Check M2_HOME
        if ($env:M2_HOME) {
            Write-Host "   ✓ M2_HOME set: $env:M2_HOME" -ForegroundColor Green
        } else {
            Write-Host "   ⚠ M2_HOME not set (optional)" -ForegroundColor DarkGray
        }
        $mavenOk = $true
    } catch {
        Write-Host "   ✗ Error checking Maven version" -ForegroundColor Red
        $mavenOk = $false
    }
} else {
    Write-Host "   ✗ Maven not found in PATH!" -ForegroundColor Red
    Write-Host "   → Install Maven: choco install maven" -ForegroundColor Yellow
    Write-Host "   → Or download from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    $mavenOk = $false
}
Write-Host ""

# Check MySQL
Write-Host "3. Checking MySQL Installation..." -ForegroundColor Yellow
Write-Host "   ----------------------------------------" -ForegroundColor DarkGray
$mysqlService = Get-Service -Name MySQL* -ErrorAction SilentlyContinue
if ($mysqlService) {
    Write-Host "   ✓ MySQL service found: $($mysqlService.Name)" -ForegroundColor Green
    if ($mysqlService.Status -eq 'Running') {
        Write-Host "   ✓ MySQL is RUNNING" -ForegroundColor Green
        
        # Try to check MySQL version
        if (Test-Command mysql) {
            try {
                $mysqlVersion = mysql --version 2>&1
                Write-Host "   ✓ MySQL CLI available: $mysqlVersion" -ForegroundColor Green
            } catch {
                Write-Host "   ⚠ MySQL CLI not in PATH" -ForegroundColor Yellow
            }
        }
        $mysqlOk = $true
    } else {
        Write-Host "   ⚠ MySQL service found but NOT RUNNING" -ForegroundColor Yellow
        Write-Host "   → Current status: $($mysqlService.Status)" -ForegroundColor Yellow
        Write-Host "   → Start MySQL: net start $($mysqlService.Name)" -ForegroundColor Yellow
        $mysqlOk = $false
    }
} else {
    # Check for Docker MySQL
    if (Test-Command docker) {
        $dockerMysql = docker ps -a --filter "name=mysql" --format "{{.Names}}" 2>&1
        if ($dockerMysql) {
            Write-Host "   ✓ MySQL Docker container found: $dockerMysql" -ForegroundColor Green
            $dockerStatus = docker ps --filter "name=mysql" --format "{{.Status}}" 2>&1
            if ($dockerStatus -like "Up*") {
                Write-Host "   ✓ Docker MySQL is RUNNING" -ForegroundColor Green
                $mysqlOk = $true
            } else {
                Write-Host "   ⚠ Docker MySQL is stopped" -ForegroundColor Yellow
                Write-Host "   → Start: docker start $dockerMysql" -ForegroundColor Yellow
                $mysqlOk = $false
            }
        } else {
            Write-Host "   ✗ MySQL not found (neither service nor Docker)" -ForegroundColor Red
            Write-Host "   → Install MySQL: choco install mysql" -ForegroundColor Yellow
            Write-Host "   → Or use Docker: docker run --name mysql-db -e MYSQL_ROOT_PASSWORD=password -p 3306:3306 -d mysql:8.0" -ForegroundColor Yellow
            $mysqlOk = $false
        }
    } else {
        Write-Host "   ✗ MySQL service not found!" -ForegroundColor Red
        Write-Host "   → Install MySQL: choco install mysql" -ForegroundColor Yellow
        Write-Host "   → Or download from: https://dev.mysql.com/downloads/mysql/" -ForegroundColor Yellow
        $mysqlOk = $false
    }
}
Write-Host ""

# Check Git (Bonus)
Write-Host "4. Checking Git Installation..." -ForegroundColor Yellow
Write-Host "   ----------------------------------------" -ForegroundColor DarkGray
if (Test-Command git) {
    $gitVersion = git --version 2>&1
    Write-Host "   ✓ Git found: $gitVersion" -ForegroundColor Green
} else {
    Write-Host "   ⚠ Git not found (optional for runtime)" -ForegroundColor DarkGray
}
Write-Host ""

# Check Database
Write-Host "5. Checking Database Configuration..." -ForegroundColor Yellow
Write-Host "   ----------------------------------------" -ForegroundColor DarkGray
if ($mysqlOk -and (Test-Command mysql)) {
    try {
        $dbCheck = mysql -u root -p"password" -e "SHOW DATABASES LIKE 'rent_management';" 2>&1
        if ($dbCheck -like "*rent_management*") {
            Write-Host "   ✓ Database 'rent_management' exists" -ForegroundColor Green
            $dbOk = $true
        } else {
            Write-Host "   ⚠ Database 'rent_management' not found" -ForegroundColor Yellow
            Write-Host "   → Create: mysql -u root -p -e ""CREATE DATABASE rent_management;""" -ForegroundColor Yellow
            $dbOk = $false
        }
    } catch {
        Write-Host "   ⚠ Could not connect to MySQL (check credentials)" -ForegroundColor Yellow
        Write-Host "   → Default credentials in application.properties: root/password" -ForegroundColor DarkGray
        $dbOk = $false
    }
} else {
    Write-Host "   ⚠ MySQL not running - cannot check database" -ForegroundColor Yellow
    $dbOk = $false
}
Write-Host ""

# Summary
Write-Host "==================================================================" -ForegroundColor Cyan
Write-Host "                          SUMMARY                                  " -ForegroundColor Cyan
Write-Host "==================================================================" -ForegroundColor Cyan
Write-Host ""

$allReady = $javaOk -and $mavenOk -and $mysqlOk

Write-Host "Component Status:" -ForegroundColor White
Write-Host "   Java 17:    $(if ($javaOk) { '✓ OK' } else { '✗ MISSING/WRONG VERSION' })" -ForegroundColor $(if ($javaOk) { 'Green' } else { 'Red' })
Write-Host "   Maven:      $(if ($mavenOk) { '✓ OK' } else { '✗ MISSING' })" -ForegroundColor $(if ($mavenOk) { 'Green' } else { 'Red' })
Write-Host "   MySQL:      $(if ($mysqlOk) { '✓ OK' } else { '✗ MISSING/NOT RUNNING' })" -ForegroundColor $(if ($mysqlOk) { 'Green' } else { 'Red' })
Write-Host "   Database:   $(if ($dbOk) { '✓ OK' } else { '⚠ NOT CREATED' })" -ForegroundColor $(if ($dbOk) { 'Green' } else { 'Yellow' })
Write-Host ""

if ($allReady) {
    Write-Host "==================================================================" -ForegroundColor Green
    Write-Host "   ✓ ENVIRONMENT IS READY FOR DEVELOPMENT!" -ForegroundColor Green
    Write-Host "==================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next Steps:" -ForegroundColor Cyan
    Write-Host "   1. Create database (if not done):" -ForegroundColor White
    Write-Host "      mysql -u root -p -e ""CREATE DATABASE rent_management;""" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "   2. Build the project:" -ForegroundColor White
    Write-Host "      mvn clean compile" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "   3. Run the application:" -ForegroundColor White
    Write-Host "      mvn spring-boot:run" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "   4. Access the API:" -ForegroundColor White
    Write-Host "      http://localhost:8080" -ForegroundColor DarkGray
    Write-Host ""
    Write-Host "   5. Test endpoints:" -ForegroundColor White
    Write-Host "      curl http://localhost:8080/owners" -ForegroundColor DarkGray
    Write-Host ""
} else {
    Write-Host "==================================================================" -ForegroundColor Red
    Write-Host "   ✗ ENVIRONMENT SETUP INCOMPLETE" -ForegroundColor Red
    Write-Host "==================================================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Required Actions:" -ForegroundColor Cyan
    Write-Host ""
    
    if (-not $javaOk) {
        Write-Host "   [ ] Install Java 17:" -ForegroundColor Yellow
        Write-Host "       choco install temurin17" -ForegroundColor White
        Write-Host "       OR download from: https://adoptium.net/" -ForegroundColor DarkGray
        Write-Host ""
    }
    
    if (-not $mavenOk) {
        Write-Host "   [ ] Install Maven:" -ForegroundColor Yellow
        Write-Host "       choco install maven" -ForegroundColor White
        Write-Host "       OR download from: https://maven.apache.org/" -ForegroundColor DarkGray
        Write-Host ""
    }
    
    if (-not $mysqlOk) {
        Write-Host "   [ ] Install/Start MySQL:" -ForegroundColor Yellow
        Write-Host "       choco install mysql" -ForegroundColor White
        Write-Host "       net start MySQL" -ForegroundColor White
        Write-Host "       OR download from: https://dev.mysql.com/" -ForegroundColor DarkGray
        Write-Host ""
    }
    
    Write-Host "   After installing missing components:" -ForegroundColor Cyan
    Write-Host "   1. Restart PowerShell" -ForegroundColor White
    Write-Host "   2. Run this script again: .\setup-environment.ps1" -ForegroundColor White
    Write-Host ""
}

Write-Host "==================================================================" -ForegroundColor Cyan
Write-Host ""

# Quick install option
if (-not $allReady) {
    Write-Host "Quick Install (requires Chocolatey):" -ForegroundColor Yellow
    $response = Read-Host "Do you want to install missing components now? (Y/N)"
    
    if ($response -eq 'Y' -or $response -eq 'y') {
        Write-Host ""
        Write-Host "Installing components..." -ForegroundColor Cyan
        
        if (-not $javaOk) {
            Write-Host "Installing Java 17..." -ForegroundColor Yellow
            choco install temurin17 -y
        }
        
        if (-not $mavenOk) {
            Write-Host "Installing Maven..." -ForegroundColor Yellow
            choco install maven -y
        }
        
        if (-not $mysqlOk) {
            Write-Host "Installing MySQL..." -ForegroundColor Yellow
            choco install mysql -y
        }
        
        Write-Host ""
        Write-Host "Installation complete!" -ForegroundColor Green
        Write-Host "Please restart PowerShell and run this script again." -ForegroundColor Yellow
        Write-Host ""
    }
}
