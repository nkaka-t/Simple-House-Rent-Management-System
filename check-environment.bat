@echo off
echo.
echo ================================================
echo    Simple House Rent Management System - Setup
echo ================================================
echo.

REM Check if Java is available
echo 1. Checking Java Installation...
java -version >nul 2>&1
if %errorlevel% == 0 (
    echo    [PASS] Java is installed
    for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
        set JAVA_VERSION=%%g
    )
    echo    Java version: %JAVA_VERSION%
    if "%JAVA_VERSION%" gtr "17" (
        echo    [WARNING] Java version is newer than required (17)
    ) else if "%JAVA_VERSION%" lss "17" (
        echo    [FAIL] Java version is older than required (17)
    ) else (
        echo    [PASS] Java 17 detected - Perfect!
    )
) else (
    echo    [FAIL] Java is not installed or not in PATH
    echo    Install Java 17 from https://adoptium.net/
)

echo.

REM Check if Maven is available
echo 2. Checking Maven Installation...
mvn --version >nul 2>&1
if %errorlevel% == 0 (
    echo    [PASS] Maven is installed
    for /f "tokens=3" %%g in ('mvn --version ^| findstr /i "Apache"') do (
        set MAVEN_VERSION=%%g
    )
    echo    Maven version: %MAVEN_VERSION%
) else (
    echo    [FAIL] Maven is not installed or not in PATH
    echo    Install Maven from https://maven.apache.org/
)

echo.

REM Check if MySQL is available
echo 3. Checking MySQL Installation...
mysql --version >nul 2>&1
if %errorlevel% == 0 (
    echo    [PASS] MySQL client is installed
    REM Try to connect to MySQL server
    echo SELECT 1; | mysql -u root -p >nul 2>&1
    if %errorlevel% == 0 (
        echo    [PASS] MySQL server connection successful
    ) else (
        echo    [WARNING] Cannot connect to MySQL server
        echo    Make sure MySQL service is running and credentials are correct
    )
) else (
    echo    [FAIL] MySQL client is not installed or not in PATH
    echo    Install MySQL from https://dev.mysql.com/
)

echo.

REM Check for database existence
echo 4. Checking Database Configuration...
mysql -u root -p -e "SHOW DATABASES LIKE 'rent_management';" >nul 2>&1
if %errorlevel% == 0 (
    echo    [PASS] Database 'rent_management' exists
) else (
    echo    [FAIL] Database 'rent_management' does not exist
    echo    Create it with: mysql -u root -p -e "CREATE DATABASE rent_management;"
)

echo.

REM Summary
echo ================================================
echo                    SUMMARY
echo ================================================
echo.

if exist "%JAVA_HOME%\bin\java.exe" if exist "%M2_HOME%\bin\mvn.cmd" (
    echo Environment appears to be properly configured!
    echo.
    echo To build and run the application:
    echo   cd C:\projects\Teta\simple-house-rent-management-system
    echo   mvn clean compile
    echo   mvn spring-boot:run
    echo.
    echo Access the API at: http://localhost:8080
) else (
    echo Environment setup incomplete. Check the FAIL entries above.
    echo.
    echo Follow the instructions in MANUAL_INSTALLATION_GUIDE.md
)

echo.
pause