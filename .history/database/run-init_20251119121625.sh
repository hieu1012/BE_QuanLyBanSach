#!/bin/bash

# =====================================================
# DATABASE INITIALIZATION SCRIPT FOR LINUX/MAC
# Quản Lý Bán Sách Application
# =====================================================

# Get script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Database configuration
DB_HOST="localhost"
DB_PORT="3306"
DB_USER="root"
DB_PASSWORD="root"
SQL_FILE="$SCRIPT_DIR/init-database.sql"

echo ""
echo "====================================================="
echo "DATABASE INITIALIZATION SCRIPT"
echo "Quản Lý Bán Sách Application"
echo "====================================================="
echo ""

# Check if SQL file exists
if [ ! -f "$SQL_FILE" ]; then
    echo "Error: File not found - $SQL_FILE"
    echo ""
    echo "Please make sure the init-database.sql file exists in the same directory."
    exit 1
fi

# Check if mysql command exists
if ! command -v mysql &> /dev/null; then
    echo "Error: mysql command not found!"
    echo ""
    echo "Please make sure MySQL/MariaDB client is installed."
    echo "You can install it using:"
    echo "  Ubuntu/Debian: sudo apt-get install mysql-client"
    echo "  macOS: brew install mysql-client"
    echo ""
    exit 1
fi

echo "====================================================="
echo "Connecting to database server..."
echo "Host: $DB_HOST"
echo "Port: $DB_PORT"
echo "User: $DB_USER"
echo "====================================================="
echo ""

# Execute SQL script
mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" < "$SQL_FILE"

if [ $? -ne 0 ]; then
    echo ""
    echo "Error: Failed to execute database initialization!"
    echo "Please check your MySQL/MariaDB connection settings."
    echo ""
    exit 1
fi

echo ""
echo "====================================================="
echo "SUCCESS: Database initialization completed!"
echo "====================================================="
echo ""
echo "The following were created:"
echo "- Database: quanlybansach"
echo "- Tables: users, categories, products, orders, order_items, order_addresses"
echo "- Sample data has been inserted"
echo ""
