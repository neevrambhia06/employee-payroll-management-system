@echo off
title Employee Payroll Management System
color 0B
cls
echo.
echo  ╔═══════════════════════════════════════════════════════╗
echo  ║     EMPLOYEE PAYROLL MANAGEMENT SYSTEM                 ║
echo  ╚═══════════════════════════════════════════════════════╝
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed!
    echo Download from: https://www.java.com/download
    pause
    exit /b 1
)

echo [1/3] Downloading libraries if needed...
if not exist lib mkdir lib

REM Download SQLite JDBC
if not exist lib\sqlite-jdbc-3.42.0.0.jar (
    echo     Downloading SQLite JDBC...
    powershell -Command "try { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar' -OutFile 'lib\sqlite-jdbc-3.42.0.0.jar' -UseBasicParsing -ErrorAction Stop } catch { exit 1 }"
    if errorlevel 1 (
        echo     Download failed!
        pause
        exit /b 1
    )
)
REM Download Gson
if not exist lib\gson-2.10.1.jar (
    echo     Downloading Gson...
    powershell -Command "try { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar' -OutFile 'lib\gson-2.10.1.jar' -UseBasicParsing -ErrorAction Stop } catch { exit 1 }"
    if errorlevel 1 (
        echo     Download failed!
        pause
        exit /b 1
    )
)
REM Download Servlet API (needed for servlet classes)
if not exist lib\javax.servlet-api-4.0.1.jar (
    echo     Downloading Servlet API...
    powershell -Command "try { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar' -OutFile 'lib\javax.servlet-api-4.0.1.jar' -UseBasicParsing -ErrorAction Stop } catch { exit 1 }"
    if errorlevel 1 (
        echo     Download failed!
        pause
        exit /b 1
    )
)

echo [2/3] Compiling...
if not exist classes mkdir classes
javac -encoding UTF-8 -d classes -cp "lib/*" src/main/java/com/payroll/database/*.java src/main/java/com/payroll/model/*.java src/main/java/com/payroll/dao/*.java src/main/java/com/payroll/SimpleServer.java 2>compile_error.txt
if errorlevel 1 (
    echo.
    echo ERROR: Compilation failed!
    if exist compile_error.txt (
        echo.
        echo Compilation errors:
        type compile_error.txt
        del compile_error.txt
    )
    pause
    exit /b 1
)
if exist compile_error.txt del compile_error.txt

echo [3/3] Copying webapp files...
xcopy /E /I /Y /Q webapp classes\webapp >nul 2>&1

echo.
echo  ╔═══════════════════════════════════════════════════════╗
echo  ║     Starting server...                                ║
echo  ╚═══════════════════════════════════════════════════════╝
echo.
java -cp "classes;lib/*" com.payroll.SimpleServer

pause


