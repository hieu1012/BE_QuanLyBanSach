#!/bin/bash

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Base URL
BASE_URL="http://localhost:8081"

echo -e "${BLUE}╔════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║     QuanLyBanSach - Product API Test Suite             ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════╝${NC}\n"

# Test 1: Get All Products
echo -e "${YELLOW}1. Get All Products (No Pagination)${NC}"
curl -s "$BASE_URL/products" | jq . || echo "Failed to get products"
echo -e "\n"

# Test 2: Get Product By ID
echo -e "${YELLOW}2. Get Product By ID (ID=1)${NC}"
curl -s "$BASE_URL/products/1" | jq . || echo "Product not found"
echo -e "\n"

# Test 3: Get Products By Category
echo -e "${YELLOW}3. Get Products By Category (CategoryID=1)${NC}"
curl -s "$BASE_URL/products/category/1" | jq . || echo "No products in category"
echo -e "\n"

# Test 4: Get All Products With Pagination
echo -e "${YELLOW}4. Get All Products With Pagination (Page 0, Size 10)${NC}"
curl -s "$BASE_URL/products/hasPage?page=0&size=10" | jq . || echo "Failed"
echo -e "\n"

# Test 5: Search By Keyword
echo -e "${YELLOW}5. Search By Keyword (keyword=java)${NC}"
curl -s "$BASE_URL/products/hasPage?page=0&size=10&keyword=java" | jq . || echo "No results"
echo -e "\n"

# Test 6: Filter By Category
echo -e "${YELLOW}6. Filter By Category (categoryId=1)${NC}"
curl -s "$BASE_URL/products/hasPage?page=0&size=10&categoryId=1" | jq . || echo "No products"
echo -e "\n"

# Test 7: Filter By Price Range
echo -e "${YELLOW}7. Filter By Price Range (100000-500000)${NC}"
curl -s "$BASE_URL/products/hasPage?page=0&size=10&minPrice=100000&maxPrice=500000" | jq . || echo "No products"
echo -e "\n"

# Test 8: Filter By Stock
echo -e "${YELLOW}8. Filter By Stock (minStock=5)${NC}"
curl -s "$BASE_URL/products/hasPage?page=0&size=10&minStock=5" | jq . || echo "No products"
echo -e "\n"

# Test 9: Filter: Keyword + Price
echo -e "${YELLOW}9. Filter: Keyword + Price${NC}"
curl -s "$BASE_URL/products/hasPage?page=0&size=10&keyword=java&minPrice=100000&maxPrice=500000" | jq . || echo "No results"
echo -e "\n"

# Test 10: Filter: Category + Price
echo -e "${YELLOW}10. Filter: Category + Price${NC}"
curl -s "$BASE_URL/products/hasPage?page=0&size=10&categoryId=1&minPrice=100000&maxPrice=500000" | jq . || echo "No results"
echo -e "\n"

# Test 11: Filter: Category + Stock
echo -e "${YELLOW}11. Filter: Category + Stock${NC}"
curl -s "$BASE_URL/products/hasPage?page=0&size=10&categoryId=1&minStock=5" | jq . || echo "No results"
echo -e "\n"

# Test 12: Filter: Price + Stock
echo -e "${YELLOW}12. Filter: Price + Stock${NC}"
curl -s "$BASE_URL/products/hasPage?page=0&size=10&minPrice=100000&maxPrice=500000&minStock=5" | jq . || echo "No results"
echo -e "\n"

# Test 13: Filter: All Filters Combined
echo -e "${YELLOW}13. Filter: All Filters (Keyword + Category + Price + Stock)${NC}"
curl -s "$BASE_URL/products/hasPage?page=0&size=10&keyword=java&categoryId=1&minPrice=100000&maxPrice=500000&minStock=5" | jq . || echo "No results"
echo -e "\n"

echo -e "${GREEN}✓ All Read Tests Completed!${NC}\n"
echo -e "${YELLOW}Note: To test CREATE/UPDATE/DELETE, you need a valid JWT token${NC}\n"
