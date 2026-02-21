# Manual Installation Guide
**Setting up Java 17, Maven, and MySQL without Chocolatey**

---

## 📋 Prerequisites Installation (Manual Method)

Since Chocolatey is not available, you'll need to install the components manually.

### 1. Install Java 17 (Manual Method)

**Step 1: Download Java 17**
- Go to: https://adoptium.net/temurin/releases/?version=17
- Select: Windows x64 (for 64-bit Windows)
- Download: `OpenJDK17U-jdk_x64_windows_hotspot_XX.X.X.XX.zip`

**Step 2: Install Java 17**
- Extract the zip file to: `C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot`
- If "Program Files" folder doesn't exist, create it first

**Step 3: Set Environment Variables**
1. Open "System Properties":
   - Press `Win + R`, type `sysdm.cpl`, press Enter
   - Click "Advanced" tab
   - Click "Environment Variables"

2. Add new System Variable:
   - Click "New" under "System Variables"
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot`
   - Click OK

3. Update PATH variable:
   - Find "Path" in System Variables
   - Click "Edit"
   - Click "New"
   - Add: `%JAVA_HOME%\bin`
   - Click OK

**Step 4: Verify Installation**
```cmd
# Open a NEW command prompt window and run:
java -version
```

Expected output should show Java 17, not Java 24.

---

### 2. Install Maven (Manual Method)

**Step 1: Download Maven**
- Go to: https://maven.apache.org/download.cgi
- Download: `apache-maven-3.9.x-bin.zip`

**Step 2: Install Maven**
- Extract to: `C:\apache-maven-3.9.x`
- Keep the full version number in the folder name

**Step 3: Set Environment Variables**
1. Open "System Properties" (same way as for Java)
2. Add new System Variable:
   - Variable name: `M2_HOME`
   - Variable value: `C:\apache-maven-3.9.x`
   - Click OK

3. Update PATH variable:
   - Find "Path" in System Variables
   - Click "Edit"
   - Click "New"
   - Add: `%M2_HOME%\bin`
   - Click OK

**Step 4: Verify Installation**
```cmd
# Open a NEW command prompt window and run:
mvn --version
```

---

### 3. Install MySQL (Manual Method)

**Step 1: Download MySQL**
- Go to: https://dev.mysql.com/downloads/mysql/
- Select: Windows (x86, 64-bit), ZIP Archive
- Download: `mysql-8.0.xx-winx64.zip`

**Step 2: Install MySQL**
- Extract to: `C:\mysql`
- Create data directory: `C:\mysql\data`
- Create config file: `C:\mysql\my.ini`

**Step 3: Create MySQL Configuration File**
Create file: `C:\mysql\my.ini`
```ini
[mysqld]
# Set basedir to your MySQL installation path
basedir=C:\\mysql
# Set datadir to your MySQL data directory
datadir=C:\\mysql\\data
port=3306
```

**Step 4: Initialize MySQL**
```cmd
# Open Command Prompt AS ADMINISTRATOR
cd C:\mysql\bin

# Initialize MySQL (first time only)
mysqld --initialize --console

# Look for temporary password in the output - it will be shown after the initialization
# Save this temporary password!
```

**Step 5: Install MySQL as a Service**
```cmd
# Still in C:\mysql\bin as Administrator
mysqld --install

# Start the service
net start mysql
```

**Step 6: Secure MySQL Installation**
```cmd
# In C:\mysql\bin
mysql -u root -p

# Enter the temporary password when prompted
# Then run:
ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';
FLUSH PRIVILEGES;
EXIT;
```

**Step 7: Verify Installation**
```cmd
mysql -u root -p
# Enter password: password
# Should connect successfully
```

---

## 🏗️ Alternative: Use MySQL via Docker (If Docker is available)

If you have Docker installed:
```powershell
# Pull and run MySQL container
docker run --name mysql-rent-system -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=rent_management -p 3306:3306 -d mysql:8.0

# Verify it's running
docker ps
```

---

## 🚀 Once All Components Are Installed

### Step 1: Verify All Installations
```cmd
# Open a NEW command prompt window
java -version    # Should show Java 17
mvn --version    # Should show Maven version
mysql --version  # Should show MySQL version
```

### Step 2: Create Database
```cmd
mysql -u root -p
# Enter password: password
```

In MySQL prompt:
```sql
CREATE DATABASE rent_management;
SHOW DATABASES;
EXIT;
```

### Step 3: Update Application Properties
Check file: `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rent_management
spring.datasource.username=root
spring.datasource.password=password
```

### Step 4: Build and Run the Application
```cmd
cd C:\projects\Teta\simple-house-rent-management-system
mvn clean compile
mvn spring-boot:run
```

---

## 🔧 Troubleshooting Common Issues

### Issue 1: Java Still Shows Version 24 After Installing Java 17
**Cause**: PATH still pointing to Java 24
**Solution**: 
- Check that `%JAVA_HOME%\bin` is at the beginning of your PATH
- Or temporarily set in command prompt:
```cmd
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
java -version
```

### Issue 2: Maven Command Not Found
**Cause**: PATH not updated or wrong location
**Solution**:
- Verify M2_HOME points to correct directory
- Verify `%M2_HOME%\bin` is in PATH
- Restart command prompt after changes

### Issue 3: MySQL Service Won't Start
**Cause**: Port conflict or installation issue
**Solution**:
- Make sure no other MySQL is running
- Check Windows Services for any MySQL service
- Stop/delete old MySQL services before installing new one

### Issue 4: Cannot Connect to MySQL
**Cause**: Wrong password or service not running
**Solution**:
```cmd
# Check if service is running
sc query mysql

# Start service
net start mysql
```

---

## 📦 Quick Download Links

### Java 17 (Eclipse Temurin)
https://adoptium.net/temurin/releases/?version=17&os=windows&arch=x64&package=jdk

### Apache Maven
https://maven.apache.org/download.cgi

### MySQL Community Server
https://dev.mysql.com/downloads/mysql/

---

## ⚡ Time-Saving Tips

### Option 1: Use SDKMAN! (If you have WSL/WSL2)
If you have WSL (Windows Subsystem for Linux):
```bash
# Install WSL Ubuntu
# Then in WSL:
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.x-tem
sdk install maven
```

### Option 2: Use Windows Package Managers
If you can install a package manager:

**Scoop**:
```powershell
# Install Scoop first
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
irm get.scoop.sh | iex

# Install packages
scoop install openjdk17 maven mysql
```

---

## 🧪 Verification Commands

Once everything is installed, run these to verify:
```cmd
# Check Java
java -version

# Check Maven
mvn --version

# Check MySQL connection
mysql -u root -p -e "SELECT VERSION();"

# Check database exists
mysql -u root -p -e "SHOW DATABASES LIKE 'rent_management';"
```

---

## 🏁 Final Steps

After installing all components:

1. **Verify installations** with commands above
2. **Create the database** if not done
3. **Navigate to project**: `cd C:\projects\Teta\simple-house-rent-management-system`
4. **Build**: `mvn clean compile`
5. **Run**: `mvn spring-boot:run`
6. **Test**: Go to `http://localhost:8080/owners` in browser

---

**Estimated Time**: 1-2 hours for manual installation  
**Difficulty**: Moderate (requires admin access and patience)  
**Success Rate**: Very High with these instructions

---

*Good luck with the installation! Once complete, your Spring Boot application will be fully functional.*