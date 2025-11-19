@echo off
REM =====================================================
REM DATABASE INITIALIZATION SCRIPT FOR WINDOWS
REM Quản Lý Bán Sách Application
REM =====================================================

setlocal enabledelayedexpansion

echo.
echo =====================================================
echo DATABASE INITIALIZATION SCRIPT
echo Quản Lý Bán Sách Application
echo =====================================================
echo.

REM Get current directory
set "SCRIPT_DIR=%~dp0"

REM Database configuration
set "DB_HOST=localhost"
set "DB_PORT=3306"
set "DB_USER=root"
set "DB_PASSWORD=root"
set "SQL_FILE=%SCRIPT_DIR%init-database.sql"

REM Check if SQL file exists
if not exist "%SQL_FILE%" (
    echo Error: File not found - %SQL_FILE%
    echo.
    echo Please make sure the init-database.sql file exists in the same directory.
    pause
    exit /b 1
)

echo Checking for mysql command...
where mysql >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: mysql command not found!
    echo.
    echo Please make sure MySQL/MariaDB client is installed and added to PATH.
    echo You can download it from:
    echo   - MySQL: https://dev.mysql.com/downloads/mysql/
    echo   - MariaDB: https://mariadb.org/download/
    echo.
    pause
    exit /b 1
)

echo.
echo =====================================================
echo Connecting to database server...
echo Host: %DB_HOST%
echo Port: %DB_PORT%
echo User: %DB_USER%
echo =====================================================
echo.

REM Execute SQL script
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% < "%SQL_FILE%"

if %errorlevel% neq 0 (
    echo.
    echo Error: Failed to execute database initialization!
    echo Please check your MySQL/MariaDB connection settings.
    echo.
    pause
    exit /b 1
)

echo.
echo =====================================================
echo SUCCESS: Database initialization completed!
echo =====================================================
echo.
echo The following were created:
echo - Database: quanlybansach
echo - Tables: users, categories, products, orders, order_items, order_addresses
echo - Sample data has been inserted
echo.
pause
