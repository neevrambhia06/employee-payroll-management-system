@echo off
title Employee Payroll Management System - Setup
color 0B
cls
echo.
echo  ╔═══════════════════════════════════════════════════════╗
echo  ║     EMPLOYEE PAYROLL MANAGEMENT SYSTEM                 ║
echo  ║                  SETUP WIZARD                          ║
echo  ╚═══════════════════════════════════════════════════════╝
echo.

REM Check if Java is installed
echo [Step 1/5] Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo.
    echo  ╔═══════════════════════════════════════════════════════╗
    echo  ║     ERROR: Java is not installed!                     ║
    echo  ╚═══════════════════════════════════════════════════════╝
    echo.
    echo Please install Java 8 or higher from:
    echo https://www.java.com/download
    echo.
    pause
    exit /b 1
)
echo     Java is installed! ✓
echo.

REM Create lib directory
echo [Step 2/5] Setting up directories...
if not exist lib mkdir lib
if not exist classes mkdir classes
echo     Directories created! ✓
echo.

REM Download dependencies
echo [Step 3/5] Downloading required libraries...
echo.
echo     This may take a few minutes on first run...
echo.

REM Download SQLite JDBC
echo     Downloading SQLite JDBC driver...
if not exist lib\sqlite-jdbc-3.42.0.0.jar (
    powershell -Command "try { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar' -OutFile 'lib\sqlite-jdbc-3.42.0.0.jar' -UseBasicParsing -ErrorAction Stop; Write-Host '      Downloaded successfully! ✓' -ForegroundColor Green } catch { Write-Host '      Download failed!' -ForegroundColor Red; exit /b 1 }"
) else (
    echo      Already exists! ✓
)

REM Download Gson
echo     Downloading Gson library...
if not exist lib\gson-2.10.1.jar (
    powershell -Command "try { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar' -OutFile 'lib\gson-2.10.1.jar' -UseBasicParsing -ErrorAction Stop; Write-Host '      Downloaded successfully! ✓' -ForegroundColor Green } catch { Write-Host '      Download failed!' -ForegroundColor Red; exit /b 1 }"
) else (
    echo      Already exists! ✓
)

REM Download Servlet API
echo     Downloading Servlet API...
if not exist lib\javax.servlet-api-4.0.1.jar (
    powershell -Command "try { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar' -OutFile 'lib\javax.servlet-api-4.0.1.jar' -UseBasicParsing -ErrorAction Stop; Write-Host '      Downloaded successfully! ✓' -ForegroundColor Green } catch { Write-Host '      Download failed!' -ForegroundColor Red; exit /b 1 }"
) else (
    echo      Already exists! ✓
)

echo.
echo  ╔═══════════════════════════════════════════════════════╗
echo  ║     All libraries downloaded successfully!               ║
echo  ╚═══════════════════════════════════════════════════════╝
echo.

REM Compile the project
echo [Step 4/5] Compiling project...
javac -encoding UTF-8 -d classes -cp "lib/*" src/main/java/com/payroll/database/*.java src/main/java/com/payroll/model/*.java src/main/java/com/payroll/dao/*.java src/main/java/com/payroll/SimpleServer.java 2>compile_error.txt

if errorlevel 1 (
    echo.
    echo  ╔═══════════════════════════════════════════════════════╗
    echo  ║     ERROR: Compilation failed!                         ║
    echo  ╚═══════════════════════════════════════════════════════╝
    echo.
    echo Checking compilation errors...
    if exist compile_error.txt (
        type compile_error.txt
        del compile_error.txt
    )
    echo.
    echo Please check the errors above and try again.
    echo.
    pause
    exit /b 1
) else (
    del compile_error.txt 2>nul
    echo     Compilation successful! ✓
)

echo.

REM Copy webapp files
echo [Step 5/5] Copying web application files...
xcopy /E /I /Y /Q webapp classes\webapp >nul 2>&1
if errorlevel 1 (
    echo     Warning: Could not copy webapp files!
) else (
    echo     Web files copied! ✓
)

echo.
echo  ╔═══════════════════════════════════════════════════════╗
echo  ║     Setup completed successfully!                      ║
echo  ╚═══════════════════════════════════════════════════════╝
echo.
echo Press any key to start the server...
pause >nul

echo.
echo  ╔═══════════════════════════════════════════════════════╗
echo  ║     Starting server...                                 ║
echo  ╚═══════════════════════════════════════════════════════╝
echo.

REM Start the server
java -cp "classes;lib/*" com.payroll.SimpleServer

pause

