# 📊 COMPLETE ACCOUNTING MODULE GUIDE
## Zoho-Level Accounting System - Testing, Usage & Documentation

---

# 📋 TABLE OF CONTENTS

1. [System Overview](#system-overview)
2. [Account Module Architecture](#account-module-architecture)
3. [Test Flow & Scenarios](#test-flow--scenarios)
4. [User Scenarios & Use Cases](#user-scenarios--use-cases)
5. [API Testing Guide](#api-testing-guide)
6. [Business Process Flows](#business-process-flows)
7. [Troubleshooting Guide](#troubleshooting-guide)
8. [Performance Testing](#performance-testing)
9. [Security Testing](#security-testing)
10. [Integration Testing](#integration-testing)

---

# 🏗️ SYSTEM OVERVIEW

## What is the Accounting Module?

The Accounting Module is a comprehensive double-entry bookkeeping system that provides:

- **Chart of Accounts Management** - Hierarchical account structure
- **Voucher Processing** - All types of financial transactions
- **Banking Operations** - Statement import and reconciliation
- **Financial Reporting** - Trial Balance, P&L, Balance Sheet, Cash Flow
- **Audit Trail** - Complete transaction history and reversals

## Key Benefits

✅ **Double-Entry Accuracy** - Every transaction follows accounting principles
✅ **Real-time Reporting** - Instant financial insights
✅ **Bank Reconciliation** - Automated matching with manual override
✅ **Compliance Ready** - Audit trail and approval workflows
✅ **Scalable Architecture** - Handles multiple companies and currencies

---

# 🏛️ ACCOUNT MODULE ARCHITECTURE

## Core Components

```
┌─────────────────────────────────────────────────────────────┐
│                    ACCOUNTING MODULE                        │
├─────────────────────────────────────────────────────────────┤
│  📊 Chart of Accounts                                       │
│  ├── Account Groups (Assets, Liabilities, Equity, etc.)    │
│  ├── Account SubGroups (Current Assets, Fixed Assets)      │
│  └── Ledgers (Individual accounts)                         │
├─────────────────────────────────────────────────────────────┤
│  📝 Voucher System                                          │
│  ├── Primary Vouchers (Sales, Purchase, Receipts, Payments)│
│  ├── Adjustment Vouchers (Contra, Journal, Credit/Debit)   │
│  └── Special Vouchers (Recurring, Opening Balance)         │
├─────────────────────────────────────────────────────────────┤
│  🏦 Banking Module                                          │
│  ├── Bank Statement Import (CSV, Excel, Manual)            │
│  ├── Auto-Reconciliation                                   │
│  └── Manual Reconciliation                                 │
├─────────────────────────────────────────────────────────────┤
│  📈 Financial Reports                                       │
│  ├── Trial Balance                                         │
│  ├── Profit & Loss Statement                               │
│  ├── Balance Sheet                                         │
│  └── Cash Flow Statement                                   │
├─────────────────────────────────────────────────────────────┤
│  🔒 Audit & Security                                        │
│  ├── Transaction Audit Trail                               │
│  ├── Voucher Reversal System                               │
│  └── Approval Workflows                                    │
└─────────────────────────────────────────────────────────────┘
```

## Database Structure

### Core Tables
- `account_group` - Main accounting groups
- `account_subgroup` - Sub-categories under groups
- `ledger` - Individual accounts
- `voucher` - Transaction headers
- `transaction_entry` - Double-entry details
- `bank_statement` - Bank statement data
- `bank_statement_line` - Individual bank transactions

---

# 🧪 TEST FLOW & SCENARIOS

## Test Environment Setup

### Prerequisites
```bash
# 1. Start PostgreSQL database
# 2. Run application
mvn spring-boot:run

# 3. Initialize standard chart of accounts
curl -X POST http://localhost:8080/api/v1/coa/groups/initialize
```

### Test Data Setup
```bash
# Create test bank account
curl -X POST http://localhost:8080/api/v1/coa/ledgers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Bank Account",
    "ledgerCode": "TEST001",
    "groupId": 2,
    "openingBalance": 100000.00,
    "balanceType": "DEBIT"
  }'

# Create test customer
curl -X POST http://localhost:8080/api/v1/coa/ledgers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Customer Ltd",
    "ledgerCode": "CUST001",
    "groupId": 2,
    "openingBalance": 0.00,
    "balanceType": "DEBIT"
  }'
```

## Complete Test Scenarios

### Scenario 1: Chart of Accounts Management

#### Test 1.1: Create Account Group
```bash
curl -X POST http://localhost:8080/api/v1/coa/groups \
  -H "Content-Type: application/json" \
  -d '{
    "groupName": "Test Assets",
    "groupCode": "TEST",
    "groupType": "ASSETS",
    "description": "Test asset group for validation"
  }'

# Expected Response: 201 Created with group details
```

#### Test 1.2: Create Account SubGroup
```bash
curl -X POST http://localhost:8080/api/v1/coa/subgroups \
  -H "Content-Type: application/json" \
  -d '{
    "subgroupName": "Test Current Assets",
    "subgroupCode": "TCA",
    "groupId": 1,
    "description": "Test current assets subgroup"
  }'

# Expected Response: 201 Created with subgroup details
```

#### Test 1.3: Create Ledger Account
```bash
curl -X POST http://localhost:8080/api/v1/coa/ledgers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Cash Account",
    "ledgerCode": "TCA001",
    "groupId": 1,
    "subgroupId": 1,
    "openingBalance": 50000.00,
    "balanceType": "DEBIT",
    "isActive": true
  }'

# Expected Response: 201 Created with ledger details
```

### Scenario 2: Voucher Processing

#### Test 2.1: Create Contra Voucher (Cash to Bank Transfer)
```bash
curl -X POST http://localhost:8080/api/v1/vouchers/contra \
  -H "Content-Type: application/json" \
  -d '{
    "voucherDate": "2024-01-15",
    "narration": "Transfer from Cash to Bank Account",
    "referenceNumber": "CONT001",
    "transactionEntries": [
      {
        "ledgerId": 1,
        "entryType": "CREDIT",
        "amount": 10000.00,
        "description": "Cash Account - Out"
      },
      {
        "ledgerId": 2,
        "entryType": "DEBIT",
        "amount": 10000.00,
        "description": "Bank Account - In"
      }
    ]
  }'

# Expected: Voucher created with balanced entries
```

#### Test 2.2: Create Journal Voucher (Adjustment Entry)
```bash
curl -X POST http://localhost:8080/api/v1/vouchers/journal \
  -H "Content-Type: application/json" \
  -d '{
    "voucherDate": "2024-01-16",
    "narration": "Depreciation Entry for Equipment",
    "referenceNumber": "JOUR001",
    "transactionEntries": [
      {
        "ledgerId": 3,
        "entryType": "DEBIT",
        "amount": 5000.00,
        "description": "Depreciation Expense"
      },
      {
        "ledgerId": 4,
        "entryType": "CREDIT",
        "amount": 5000.00,
        "description": "Accumulated Depreciation"
      }
    ]
  }'

# Expected: Journal voucher created successfully
```

#### Test 2.3: Create Credit Note
```bash
curl -X POST http://localhost:8080/api/v1/vouchers/credit-note \
  -H "Content-Type: application/json" \
  -d '{
    "voucherDate": "2024-01-17",
    "customerLedgerId": 5,
    "amount": 2500.00,
    "reason": "Return of goods",
    "narration": "Credit Note for returned goods",
    "referenceNumber": "CN001"
  }'

# Expected: Credit note created with proper debit/credit entries
```

### Scenario 3: Banking Operations

#### Test 3.1: Import Bank Statement
```bash
# Create a CSV file with bank statement data
echo "Date,Description,Debit,Credit,Balance
2024-01-15,Cash Deposit,0,10000,10000
2024-01-16,Payment to Vendor,5000,0,5000
2024-01-17,Interest Credit,0,100,5100" > bank_statement.csv

curl -X POST http://localhost:8080/api/v1/banking/statements/import \
  -F "bankAccountId=2" \
  -F "statementDate=2024-01-17" \
  -F "file=@bank_statement.csv" \
  -F "importSource=CSV"

# Expected: Bank statement imported with 3 lines
```

#### Test 3.2: Auto-Reconcile Bank Statement
```bash
curl -X POST http://localhost:8080/api/v1/banking/statements/1/auto-reconcile

# Expected: Automatic matching of transactions
```

#### Test 3.3: Manual Reconciliation
```bash
curl -X POST http://localhost:8080/api/v1/banking/statements/lines/1/reconcile \
  -F "transactionEntryId=1" \
  -F "notes=Manual reconciliation"

# Expected: Statement line reconciled with transaction entry
```

### Scenario 4: Financial Reports

#### Test 4.1: Generate Trial Balance
```bash
curl -X GET "http://localhost:8080/api/v1/reports/financial/trial-balance?fromDate=2024-01-01&toDate=2024-01-31"

# Expected Response:
# {
#   "reportTitle": "Trial Balance",
#   "fromDate": "2024-01-01",
#   "toDate": "2024-01-31",
#   "trialBalanceLines": [...],
#   "totalDebits": 15000.00,
#   "totalCredits": 15000.00,
#   "isBalanced": true
# }
```

#### Test 4.2: Generate Profit & Loss
```bash
curl -X GET "http://localhost:8080/api/v1/reports/financial/profit-loss?fromDate=2024-01-01&toDate=2024-01-31"

# Expected: P&L statement with income and expense details
```

#### Test 4.3: Generate Balance Sheet
```bash
curl -X GET "http://localhost:8080/api/v1/reports/financial/balance-sheet?asOfDate=2024-01-31"

# Expected: Balance sheet with assets, liabilities, and equity
```

---

# 👥 USER SCENARIOS & USE CASES

## Business User Scenarios

### Scenario A: Company Setup (New Business)

**User**: Business Owner/Accountant
**Goal**: Set up accounting system for new business

#### Step-by-Step Process:

1. **Initialize Chart of Accounts**
   ```
   Action: POST /api/v1/coa/groups/initialize
   Result: Standard accounting groups created
   ```

2. **Create Company-Specific Accounts**
   ```
   - Add company bank accounts
   - Create customer/vendor ledgers
   - Set up expense accounts
   - Configure tax accounts
   ```

3. **Record Opening Balances**
   ```
   - Create opening balance vouchers
   - Enter initial cash/bank balances
   - Record fixed assets
   - Set up capital accounts
   ```

4. **Verify Setup**
   ```
   - Generate trial balance
   - Check account balances
   - Verify opening entries
   ```

### Scenario B: Daily Operations (Sales & Purchases)

**User**: Sales Manager/Purchase Manager
**Goal**: Record daily business transactions

#### Sales Process:
1. **Create Sales Invoice**
   ```
   - Select customer ledger
   - Add line items with taxes
   - Generate invoice number
   - Save as draft/pending approval
   ```

2. **Receive Payment**
   ```
   - Create receipt voucher
   - Link to invoice
   - Update customer balance
   - Update bank account
   ```

#### Purchase Process:
1. **Record Purchase Bill**
   ```
   - Select vendor ledger
   - Add purchase details
   - Calculate taxes
   - Save for approval
   ```

2. **Make Payment**
   ```
   - Create payment voucher
   - Link to bill
   - Update vendor balance
   - Update bank account
   ```

### Scenario C: Month-End Closing

**User**: Accountant
**Goal**: Complete month-end financial closing

#### Process:
1. **Bank Reconciliation**
   ```
   - Import bank statements
   - Auto-reconcile transactions
   - Handle unmatched items
   - Confirm reconciliation
   ```

2. **Adjustment Entries**
   ```
   - Record depreciation
   - Accrue expenses
   - Adjust prepayments
   - Post provisions
   ```

3. **Generate Reports**
   ```
   - Trial Balance
   - Profit & Loss
   - Balance Sheet
   - Cash Flow Statement
   ```

4. **Review & Approve**
   ```
   - Verify all entries
   - Check report accuracy
   - Approve closing entries
   - Lock period
   ```

### Scenario D: Year-End Processing

**User**: Senior Accountant/CFO
**Goal**: Complete annual financial closing

#### Process:
1. **Pre-Year-End Review**
   ```
   - Audit all transactions
   - Verify account balances
   - Check compliance
   - Prepare adjustments
   ```

2. **Closing Entries**
   ```
   - Close revenue accounts
   - Close expense accounts
   - Transfer to retained earnings
   - Create year-end reports
   ```

3. **New Year Setup**
   ```
   - Create new fiscal year
   - Set opening balances
   - Configure new periods
   - Archive old data
   ```

---

# 🔄 BUSINESS PROCESS FLOWS

## Complete Accounting Cycle

```
┌─────────────────────────────────────────────────────────────┐
│                    ACCOUNTING CYCLE                         │
├─────────────────────────────────────────────────────────────┤
│  1. Setup Phase                                            │
│     ├── Create Chart of Accounts                           │
│     ├── Set Opening Balances                               │
│     └── Configure System Settings                          │
├─────────────────────────────────────────────────────────────┤
│  2. Transaction Recording                                  │
│     ├── Daily Sales/Purchases                             │
│     ├── Receipts/Payments                                 │
│     ├── Adjustments                                       │
│     └── Bank Transactions                                 │
├─────────────────────────────────────────────────────────────┤
│  3. Reconciliation                                         │
│     ├── Bank Statement Import                             │
│     ├── Auto-Matching                                     │
│     ├── Manual Reconciliation                             │
│     └── Exception Handling                                │
├─────────────────────────────────────────────────────────────┤
│  4. Month-End Processing                                   │
│     ├── Adjusting Entries                                 │
│     ├── Depreciation                                      │
│     ├── Accruals                                          │
│     └── Provisions                                        │
├─────────────────────────────────────────────────────────────┤
│  5. Reporting                                              │
│     ├── Trial Balance                                     │
│     ├── Profit & Loss                                     │
│     ├── Balance Sheet                                     │
│     └── Cash Flow                                         │
├─────────────────────────────────────────────────────────────┤
│  6. Review & Approval                                      │
│     ├── Internal Review                                   │
│     ├── Management Approval                               │
│     ├── Audit Trail                                       │
│     └── Period Locking                                    │
└─────────────────────────────────────────────────────────────┘
```

## Voucher Processing Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    VOUCHER PROCESSING                       │
├─────────────────────────────────────────────────────────────┤
│  1. Create Voucher                                         │
│     ├── Select Voucher Type                               │
│     ├── Enter Basic Details                               │
│     └── Add Transaction Entries                           │
├─────────────────────────────────────────────────────────────┤
│  2. Validation                                            │
│     ├── Check Double-Entry Balance                        │
│     ├── Validate Account Codes                            │
│     ├── Verify Amounts                                    │
│     └── Check Business Rules                              │
├─────────────────────────────────────────────────────────────┤
│  3. Approval Process                                       │
│     ├── Save as Draft                                     │
│     ├── Submit for Approval                               │
│     ├── Review by Manager                                 │
│     └── Approve/Reject                                    │
├─────────────────────────────────────────────────────────────┤
│  4. Posting                                               │
│     ├── Post Approved Voucher                             │
│     ├── Update Ledger Balances                            │
│     ├── Create Audit Trail                                │
│     └── Generate Voucher Number                           │
├─────────────────────────────────────────────────────────────┤
│  5. Exception Handling                                     │
│     ├── Reversal Process                                  │
│     ├── Correction Entries                                │
│     ├── Amendment Process                                 │
│     └── Audit Documentation                               │
└─────────────────────────────────────────────────────────────┘
```

---

# 🛠️ API TESTING GUIDE

## Testing Tools & Setup

### Required Tools:
- **Postman** or **curl** for API testing
- **PostgreSQL** database
- **Spring Boot** application running
- **Test data** files (CSV for bank statements)

### Environment Setup:
```bash
# 1. Start database
sudo systemctl start postgresql

# 2. Run application
mvn spring-boot:run

# 3. Verify application is running
curl http://localhost:8080/actuator/health
```

## Comprehensive API Test Suite

### Test Suite 1: Chart of Accounts

#### Test 1.1: Create Account Group
```bash
# Test Case: Valid Account Group Creation
curl -X POST http://localhost:8080/api/v1/coa/groups \
  -H "Content-Type: application/json" \
  -d '{
    "groupName": "Test Assets",
    "groupCode": "TASS",
    "groupType": "ASSETS",
    "description": "Test asset group",
    "isActive": true
  }'

# Expected: 201 Created
# Response: {"groupId": 1, "groupName": "Test Assets", ...}

# Test Case: Duplicate Group Code
curl -X POST http://localhost:8080/api/v1/coa/groups \
  -H "Content-Type: application/json" \
  -d '{
    "groupName": "Another Assets",
    "groupCode": "TASS",
    "groupType": "ASSETS",
    "description": "Duplicate code test"
  }'

# Expected: 409 Conflict
# Response: {"error": "Account group with code 'TASS' already exists"}

# Test Case: Invalid Group Type
curl -X POST http://localhost:8080/api/v1/coa/groups \
  -H "Content-Type: application/json" \
  -d '{
    "groupName": "Invalid Group",
    "groupCode": "INV",
    "groupType": "INVALID_TYPE",
    "description": "Invalid type test"
  }'

# Expected: 400 Bad Request
# Response: Validation error for groupType
```

#### Test 1.2: Get Account Groups
```bash
# Test Case: Get All Active Groups
curl -X GET http://localhost:8080/api/v1/coa/groups

# Expected: 200 OK
# Response: Array of active account groups

# Test Case: Get Groups by Type
curl -X GET http://localhost:8080/api/v1/coa/groups/type/ASSETS

# Expected: 200 OK
# Response: Array of asset groups only

# Test Case: Get Specific Group
curl -X GET http://localhost:8080/api/v1/coa/groups/1

# Expected: 200 OK
# Response: Specific group details with subgroups and ledgers
```

### Test Suite 2: Voucher Processing

#### Test 2.1: Contra Voucher Tests
```bash
# Test Case: Valid Contra Voucher
curl -X POST http://localhost:8080/api/v1/vouchers/contra \
  -H "Content-Type: application/json" \
  -d '{
    "voucherDate": "2024-01-15",
    "narration": "Cash to Bank Transfer",
    "transactionEntries": [
      {
        "ledgerId": 1,
        "entryType": "CREDIT",
        "amount": 5000.00,
        "description": "Cash Out"
      },
      {
        "ledgerId": 2,
        "entryType": "DEBIT",
        "amount": 5000.00,
        "description": "Bank In"
      }
    ]
  }'

# Expected: 201 Created
# Response: Voucher details with balanced entries

# Test Case: Unbalanced Contra Voucher
curl -X POST http://localhost:8080/api/v1/vouchers/contra \
  -H "Content-Type: application/json" \
  -d '{
    "voucherDate": "2024-01-15",
    "narration": "Unbalanced Transfer",
    "transactionEntries": [
      {
        "ledgerId": 1,
        "entryType": "CREDIT",
        "amount": 5000.00,
        "description": "Cash Out"
      },
      {
        "ledgerId": 2,
        "entryType": "DEBIT",
        "amount": 3000.00,
        "description": "Bank In"
      }
    ]
  }'

# Expected: 400 Bad Request
# Response: "Contra voucher entries are not balanced"

# Test Case: More than 2 Entries
curl -X POST http://localhost:8080/api/v1/vouchers/contra \
  -H "Content-Type: application/json" \
  -d '{
    "voucherDate": "2024-01-15",
    "narration": "Multiple Entries",
    "transactionEntries": [
      {
        "ledgerId": 1,
        "entryType": "CREDIT",
        "amount": 5000.00,
        "description": "Cash Out"
      },
      {
        "ledgerId": 2,
        "entryType": "DEBIT",
        "amount": 3000.00,
        "description": "Bank In"
      },
      {
        "ledgerId": 3,
        "entryType": "DEBIT",
        "amount": 2000.00,
        "description": "Another Account"
      }
    ]
  }'

# Expected: 400 Bad Request
# Response: "Contra voucher must have exactly 2 entries"
```

#### Test 2.2: Journal Voucher Tests
```bash
# Test Case: Valid Journal Entry
curl -X POST http://localhost:8080/api/v1/vouchers/journal \
  -H "Content-Type: application/json" \
  -d '{
    "voucherDate": "2024-01-16",
    "narration": "Depreciation Entry",
    "transactionEntries": [
      {
        "ledgerId": 10,
        "entryType": "DEBIT",
        "amount": 1000.00,
        "description": "Depreciation Expense"
      },
      {
        "ledgerId": 11,
        "entryType": "CREDIT",
        "amount": 1000.00,
        "description": "Accumulated Depreciation"
      }
    ]
  }'

# Expected: 201 Created
# Response: Journal voucher with balanced entries

# Test Case: Multi-Line Journal Entry
curl -X POST http://localhost:8080/api/v1/vouchers/journal \
  -H "Content-Type: application/json" \
  -d '{
    "voucherDate": "2024-01-16",
    "narration": "Complex Adjustment",
    "transactionEntries": [
      {
        "ledgerId": 10,
        "entryType": "DEBIT",
        "amount": 1000.00,
        "description": "Expense 1"
      },
      {
        "ledgerId": 12,
        "entryType": "DEBIT",
        "amount": 500.00,
        "description": "Expense 2"
      },
      {
        "ledgerId": 13,
        "entryType": "CREDIT",
        "amount": 1500.00,
        "description": "Accrued Expenses"
      }
    ]
  }'

# Expected: 201 Created
# Response: Multi-line journal voucher
```

### Test Suite 3: Banking Operations

#### Test 3.1: Bank Statement Import
```bash
# Create test CSV file
echo "Date,Description,Debit,Credit,Balance
2024-01-15,Cash Deposit,0,10000,10000
2024-01-16,Vendor Payment,5000,0,5000
2024-01-17,Interest Credit,0,50,5050" > test_statement.csv

# Test Case: Valid CSV Import
curl -X POST http://localhost:8080/api/v1/banking/statements/import \
  -F "bankAccountId=2" \
  -F "statementDate=2024-01-17" \
  -F "file=@test_statement.csv" \
  -F "importSource=CSV"

# Expected: 201 Created
# Response: Bank statement with 3 lines imported

# Test Case: Invalid File Format
curl -X POST http://localhost:8080/api/v1/banking/statements/import \
  -F "bankAccountId=2" \
  -F "statementDate=2024-01-17" \
  -F "file=@invalid_file.txt" \
  -F "importSource=CSV"

# Expected: 400 Bad Request
# Response: "Invalid file format"

# Test Case: Missing Required Fields
curl -X POST http://localhost:8080/api/v1/banking/statements/import \
  -F "bankAccountId=2" \
  -F "file=@test_statement.csv"

# Expected: 400 Bad Request
# Response: "Statement date is required"
```

#### Test 3.2: Bank Reconciliation
```bash
# Test Case: Auto-Reconcile
curl -X POST http://localhost:8080/api/v1/banking/statements/1/auto-reconcile

# Expected: 200 OK
# Response: Reconciliation results with matched/unmatched items

# Test Case: Manual Reconciliation
curl -X POST http://localhost:8080/api/v1/banking/statements/lines/1/reconcile \
  -F "transactionEntryId=1" \
  -F "notes=Manual match"

# Expected: 200 OK
# Response: Statement line reconciled successfully

# Test Case: Get Reconciliation Report
curl -X GET "http://localhost:8080/api/v1/banking/reconciliation/report?bankAccountId=2&fromDate=2024-01-01&toDate=2024-01-31"

# Expected: 200 OK
# Response: Detailed reconciliation report
```

### Test Suite 4: Financial Reports

#### Test 4.1: Trial Balance
```bash
# Test Case: Generate Trial Balance
curl -X GET "http://localhost:8080/api/v1/reports/financial/trial-balance?fromDate=2024-01-01&toDate=2024-01-31"

# Expected: 200 OK
# Response: Trial balance with all accounts and balances

# Test Case: Invalid Date Range
curl -X GET "http://localhost:8080/api/v1/reports/financial/trial-balance?fromDate=2024-01-31&toDate=2024-01-01"

# Expected: 400 Bad Request
# Response: "From date cannot be after to date"
```

#### Test 4.2: Profit & Loss
```bash
# Test Case: Generate P&L
curl -X GET "http://localhost:8080/api/v1/reports/financial/profit-loss?fromDate=2024-01-01&toDate=2024-01-31"

# Expected: 200 OK
# Response: P&L statement with income and expenses

# Test Case: P&L with Previous Period Comparison
curl -X GET "http://localhost:8080/api/v1/reports/financial/profit-loss?fromDate=2024-01-01&toDate=2024-01-31&compareWithPrevious=true"

# Expected: 200 OK
# Response: P&L with variance analysis
```

---

# 🔍 TROUBLESHOOTING GUIDE

## Common Issues & Solutions

### Issue 1: Voucher Not Balanced
**Problem**: "Voucher entries are not balanced"
**Solution**:
```bash
# Check debit and credit totals
# Ensure total debits = total credits
# Verify entry types are correct
```

### Issue 2: Bank Reconciliation Mismatch
**Problem**: Statement balance doesn't match book balance
**Solution**:
```bash
# Check for unrecorded transactions
# Verify transaction dates
# Look for bank charges or interest
# Review outstanding checks
```

### Issue 3: Report Generation Errors
**Problem**: Reports show incorrect data
**Solution**:
```bash
# Verify date ranges
# Check voucher posting status
# Ensure all entries are approved
# Review ledger balances
```

### Issue 4: Performance Issues
**Problem**: Slow report generation
**Solution**:
```bash
# Add database indexes
# Optimize queries
# Implement caching
# Use pagination for large datasets
```

---

# 📊 PERFORMANCE TESTING

## Load Testing Scenarios

### Test 1: Concurrent Voucher Creation
```bash
# Create 100 vouchers simultaneously
for i in {1..100}; do
  curl -X POST http://localhost:8080/api/v1/vouchers/contra \
    -H "Content-Type: application/json" \
    -d "{
      \"voucherDate\": \"2024-01-15\",
      \"narration\": \"Test Voucher $i\",
      \"transactionEntries\": [
        {
          \"ledgerId\": 1,
          \"entryType\": \"CREDIT\",
          \"amount\": 100.00,
          \"description\": \"Test Entry\"
        },
        {
          \"ledgerId\": 2,
          \"entryType\": \"DEBIT\",
          \"amount\": 100.00,
          \"description\": \"Test Entry\"
        }
      ]
    }" &
done
wait

# Expected: All 100 vouchers created successfully
# Response time: < 2 seconds per voucher
```

### Test 2: Large Report Generation
```bash
# Generate trial balance with 1000+ accounts
curl -X GET "http://localhost:8080/api/v1/reports/financial/trial-balance?fromDate=2024-01-01&toDate=2024-12-31"

# Expected: Report generated within 5 seconds
# Memory usage: < 500MB
```

---

# 🔒 SECURITY TESTING

## Authentication & Authorization Tests

### Test 1: Unauthorized Access
```bash
# Test without authentication token
curl -X GET http://localhost:8080/api/v1/coa/groups

# Expected: 401 Unauthorized
```

### Test 2: Invalid Token
```bash
# Test with invalid token
curl -X GET http://localhost:8080/api/v1/coa/groups \
  -H "Authorization: Bearer invalid_token"

# Expected: 401 Unauthorized
```

### Test 3: SQL Injection Protection
```bash
# Test SQL injection in account name
curl -X POST http://localhost:8080/api/v1/coa/groups \
  -H "Content-Type: application/json" \
  -d '{
    "groupName": "Test\"; DROP TABLE account_group; --",
    "groupCode": "TEST",
    "groupType": "ASSETS"
  }'

# Expected: 400 Bad Request (validation error)
# Database should remain intact
```

---

# 🔗 INTEGRATION TESTING

## End-to-End Test Scenarios

### Scenario 1: Complete Accounting Cycle
```bash
# 1. Setup
curl -X POST http://localhost:8080/api/v1/coa/groups/initialize

# 2. Create accounts
curl -X POST http://localhost:8080/api/v1/coa/ledgers -d '{...}'

# 3. Record transactions
curl -X POST http://localhost:8080/api/v1/vouchers/contra -d '{...}'

# 4. Import bank statement
curl -X POST http://localhost:8080/api/v1/banking/statements/import -F "..."

# 5. Reconcile
curl -X POST http://localhost:8080/api/v1/banking/statements/1/auto-reconcile

# 6. Generate reports
curl -X GET "http://localhost:8080/api/v1/reports/financial/trial-balance?..."

# Expected: Complete cycle works without errors
```

### Scenario 2: Multi-User Operations
```bash
# Test concurrent operations from multiple users
# User 1: Creating vouchers
# User 2: Reconciling bank statements
# User 3: Generating reports

# Expected: No data corruption or conflicts
```

---

# 📝 TESTING CHECKLIST

## Pre-Testing Checklist
- [ ] Database is running and accessible
- [ ] Application is started successfully
- [ ] Standard chart of accounts is initialized
- [ ] Test data is prepared
- [ ] API endpoints are accessible

## Functional Testing Checklist
- [ ] Chart of Accounts CRUD operations
- [ ] Voucher creation and validation
- [ ] Double-entry bookkeeping accuracy
- [ ] Bank statement import and reconciliation
- [ ] Financial report generation
- [ ] Approval workflow
- [ ] Audit trail functionality

## Performance Testing Checklist
- [ ] Response time < 2 seconds for API calls
- [ ] Report generation < 5 seconds
- [ ] Concurrent user handling
- [ ] Memory usage optimization
- [ ] Database query optimization

## Security Testing Checklist
- [ ] Authentication and authorization
- [ ] Input validation and sanitization
- [ ] SQL injection protection
- [ ] XSS prevention
- [ ] Data encryption
- [ ] Audit logging

## Integration Testing Checklist
- [ ] End-to-end workflow testing
- [ ] Multi-module integration
- [ ] Data consistency across modules
- [ ] Error handling and recovery
- [ ] Backup and restore procedures

---

# 🎯 CONCLUSION

This comprehensive testing and user guide provides:

✅ **Complete test scenarios** for all accounting modules
✅ **Step-by-step user workflows** for business operations
✅ **API testing examples** with expected responses
✅ **Troubleshooting solutions** for common issues
✅ **Performance and security testing** guidelines
✅ **Integration testing** scenarios

The accounting system is now ready for:
- **User Acceptance Testing (UAT)**
- **Production deployment**
- **User training and adoption**
- **Ongoing maintenance and support**

For additional support or questions, refer to the API documentation at: `http://localhost:8080/swagger-ui/index.html`

---

**Document Version**: 1.0  
**Last Updated**: January 2024  
**Next Review**: March 2024
