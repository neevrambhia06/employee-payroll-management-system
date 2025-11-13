@echo off
title Employee Payroll Management System
color 0B
cls

REM Check if this is first run or if dependencies are missing
if not exist lib\sqlite-jdbc-3.42.0.0.jar (
    goto :setup_needed
)
if not exist lib\gson-2.10.1.jar (
    goto :setup_needed
)
if not exist lib\javax.servlet-api-4.0.1.jar (
    goto :setup_needed
)

REM All dependencies exist, run directly
call RUN.bat
exit /b

:setup_needed
echo.
echo  ╔═══════════════════════════════════════════════════════╗
echo  ║   First time setup detected!                          ║
echo  ║   Downloading required libraries...                    ║
echo  ╚═══════════════════════════════════════════════════════╝
echo.
echo This will only happen once.
echo.
call SETUP.bat
exit /b

