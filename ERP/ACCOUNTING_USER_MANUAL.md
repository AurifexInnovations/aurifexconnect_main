# 📚 ACCOUNTING MODULE USER MANUAL
## Complete User Guide for Business Operations

---

# 📋 TABLE OF CONTENTS

1. [Getting Started](#getting-started)
2. [Chart of Accounts Management](#chart-of-accounts-management)
3. [Daily Transaction Recording](#daily-transaction-recording)
4. [Banking Operations](#banking-operations)
5. [Month-End Procedures](#month-end-procedures)
6. [Financial Reporting](#financial-reporting)
7. [Year-End Closing](#year-end-closing)
8. [Common Scenarios](#common-scenarios)
9. [Troubleshooting](#troubleshooting)
10. [Best Practices](#best-practices)

---

# 🚀 GETTING STARTED

## System Overview

The Accounting Module is your complete financial management system that handles:
- **Chart of Accounts** - Your account structure
- **Transaction Recording** - Daily business transactions
- **Bank Reconciliation** - Matching bank records
- **Financial Reports** - Business insights
- **Audit Trail** - Complete transaction history

## Initial Setup (First Time Users)

### Step 1: Initialize Your Chart of Accounts
```bash
# This creates standard accounting groups automatically
POST /api/v1/coa/groups/initialize
```

**What This Does:**
- Creates 11 standard account groups (Assets, Liabilities, Equity, Income, Expenses)
- Sets up 18 standard subgroups (Cash & Bank, Accounts Receivable, etc.)
- Provides a foundation for your accounting structure

### Step 2: Create Your Company Accounts
```bash
# Add your bank accounts
POST /api/v1/coa/ledgers
{
  "name": "HDFC Bank - Current Account",
  "ledgerCode": "HDFC001",
  "groupId": 2,  // Current Assets
  "subgroupId": 5,  // Cash & Bank
  "openingBalance": 100000.00,
  "balanceType": "DEBIT"
}

# Add your customers
POST /api/v1/coa/ledgers
{
  "name": "ABC Company Ltd",
  "ledgerCode": "CUST001",
  "groupId": 2,  // Current Assets
  "subgroupId": 2,  // Accounts Receivable
  "openingBalance": 0.00,
  "balanceType": "DEBIT"
}

# Add your vendors
POST /api/v1/coa/ledgers
{
  "name": "XYZ Suppliers",
  "ledgerCode": "VEND001",
  "groupId": 4,  // Current Liabilities
  "subgroupId": 8,  // Accounts Payable
  "openingBalance": 0.00,
  "balanceType": "CREDIT"
}
```

### Step 3: Record Opening Balances
```bash
# Create opening balance voucher
POST /api/v1/vouchers/journal
{
  "voucherDate": "2024-01-01",
  "narration": "Opening Balance Entry",
  "transactionEntries": [
    {
      "ledgerId": 1,  // Bank Account
      "entryType": "DEBIT",
      "amount": 100000.00,
      "description": "Opening Bank Balance"
    },
    {
      "ledgerId": 5,  // Capital Account
      "entryType": "CREDIT",
      "amount": 100000.00,
      "description": "Owner's Capital"
    }
  ]
}
```

---

# 🏛️ CHART OF ACCOUNTS MANAGEMENT

## Understanding Your Account Structure

### Account Hierarchy
```
Chart of Accounts
├── Assets
│   ├── Current Assets
│   │   ├── Cash & Bank
│   │   ├── Accounts Receivable
│   │   └── Inventory
│   ├── Fixed Assets
│   │   ├── Land & Building
│   │   └── Plant & Machinery
│   └── Investments
├── Liabilities
│   ├── Current Liabilities
│   │   ├── Accounts Payable
│   │   └── Loans & Advances
│   └── Long Term Liabilities
├── Equity
│   ├── Capital
│   └── Reserves & Surplus
├── Income
│   ├── Direct Income
│   └── Indirect Income
└── Expenses
    ├── Direct Expenses
    └── Indirect Expenses
```

## Managing Account Groups

### Adding New Account Groups
**When to Use**: When standard groups don't fit your business needs

```bash
POST /api/v1/coa/groups
{
  "groupName": "Intangible Assets",
  "groupCode": "INT",
  "groupType": "ASSETS",
  "description": "Software, patents, trademarks"
}
```

### Adding Account SubGroups
**When to Use**: When you need more specific categorization

```bash
POST /api/v1/coa/subgroups
{
  "subgroupName": "Accounts Receivable - Overdue",
  "subgroupCode": "AR_OVD",
  "groupId": 2,  // Current Assets
  "description": "Overdue customer accounts"
}
```

### Managing Ledger Accounts
**When to Use**: For individual accounts (customers, vendors, bank accounts)

```bash
POST /api/v1/coa/ledgers
{
  "name": "Microsoft Corporation",
  "ledgerCode": "CUST_MSFT",
  "groupId": 2,  // Current Assets
  "subgroupId": 2,  // Accounts Receivable
  "openingBalance": 0.00,
  "balanceType": "DEBIT",
  "email": "billing@microsoft.com",
  "phone": "+1-555-0123",
  "address": "One Microsoft Way, Redmond, WA"
}
```

## Account Codes Best Practices

### Recommended Coding Structure
- **Assets**: 1000-1999
- **Liabilities**: 2000-2999
- **Equity**: 3000-3999
- **Income**: 4000-4999
- **Expenses**: 5000-5999

### Examples
- `1001` - Cash in Hand
- `1002` - HDFC Bank Current
- `1003` - ICICI Bank Savings
- `1101` - Accounts Receivable - ABC Ltd
- `1102` - Accounts Receivable - XYZ Corp

---

# 📝 DAILY TRANSACTION RECORDING

## Sales Transactions

### Recording a Sale
**Scenario**: You sold goods worth ₹50,000 to a customer

```bash
POST /api/v1/vouchers/journal
{
  "voucherDate": "2024-01-15",
  "narration": "Sales Invoice #INV-001 to ABC Company",
  "referenceNumber": "INV-001",
  "transactionEntries": [
    {
      "ledgerId": 1101,  // ABC Company (Customer)
      "entryType": "DEBIT",
      "amount": 50000.00,
      "description": "Sales to ABC Company"
    },
    {
      "ledgerId": 4001,  // Sales Account
      "entryType": "CREDIT",
      "amount": 50000.00,
      "description": "Sales Revenue"
    }
  ]
}
```

### Receiving Payment
**Scenario**: Customer pays ₹30,000 against the invoice

```bash
POST /api/v1/vouchers/receipt
{
  "voucherDate": "2024-01-20",
  "narration": "Payment received from ABC Company",
  "referenceNumber": "RCP-001",
  "transactionEntries": [
    {
      "ledgerId": 1002,  // HDFC Bank
      "entryType": "DEBIT",
      "amount": 30000.00,
      "description": "Payment received in bank"
    },
    {
      "ledgerId": 1101,  // ABC Company
      "entryType": "CREDIT",
      "amount": 30000.00,
      "description": "Payment against invoice INV-001"
    }
  ]
}
```

## Purchase Transactions

### Recording a Purchase
**Scenario**: You bought goods worth ₹25,000 from a vendor

```bash
POST /api/v1/vouchers/journal
{
  "voucherDate": "2024-01-16",
  "narration": "Purchase Bill #BILL-001 from XYZ Suppliers",
  "referenceNumber": "BILL-001",
  "transactionEntries": [
    {
      "ledgerId": 5001,  // Purchase Account
      "entryType": "DEBIT",
      "amount": 25000.00,
      "description": "Purchase from XYZ Suppliers"
    },
    {
      "ledgerId": 2101,  // XYZ Suppliers (Vendor)
      "entryType": "CREDIT",
      "amount": 25000.00,
      "description": "Purchase Bill BILL-001"
    }
  ]
}
```

### Making Payment
**Scenario**: You pay ₹25,000 to the vendor

```bash
POST /api/v1/vouchers/payment
{
  "voucherDate": "2024-01-25",
  "narration": "Payment to XYZ Suppliers",
  "referenceNumber": "PAY-001",
  "transactionEntries": [
    {
      "ledgerId": 2101,  // XYZ Suppliers
      "entryType": "DEBIT",
      "amount": 25000.00,
      "description": "Payment against bill BILL-001"
    },
    {
      "ledgerId": 1002,  // HDFC Bank
      "entryType": "CREDIT",
      "amount": 25000.00,
      "description": "Payment made from bank"
    }
  ]
}
```

## Cash Transactions

### Cash to Bank Transfer
**Scenario**: Transfer ₹10,000 from cash to bank

```bash
POST /api/v1/vouchers/contra
{
  "voucherDate": "2024-01-18",
  "narration": "Cash deposited to bank",
  "referenceNumber": "CONT-001",
  "transactionEntries": [
    {
      "ledgerId": 1002,  // HDFC Bank
      "entryType": "DEBIT",
      "amount": 10000.00,
      "description": "Cash deposited to bank"
    },
    {
      "ledgerId": 1001,  // Cash in Hand
      "entryType": "CREDIT",
      "amount": 10000.00,
      "description": "Cash withdrawn for deposit"
    }
  ]
}
```

## Adjustment Entries

### Depreciation Entry
**Scenario**: Record monthly depreciation of ₹5,000

```bash
POST /api/v1/vouchers/journal
{
  "voucherDate": "2024-01-31",
  "narration": "Monthly depreciation - January 2024",
  "referenceNumber": "DEP-001",
  "transactionEntries": [
    {
      "ledgerId": 5201,  // Depreciation Expense
      "entryType": "DEBIT",
      "amount": 5000.00,
      "description": "Monthly depreciation"
    },
    {
      "ledgerId": 1103,  // Accumulated Depreciation
      "entryType": "CREDIT",
      "amount": 5000.00,
      "description": "Accumulated depreciation"
    }
  ]
}
```

### Accrued Expenses
**Scenario**: Record accrued salary of ₹30,000

```bash
POST /api/v1/vouchers/journal
{
  "voucherDate": "2024-01-31",
  "narration": "Accrued salary for January 2024",
  "referenceNumber": "ACC-001",
  "transactionEntries": [
    {
      "ledgerId": 5301,  // Salary Expense
      "entryType": "DEBIT",
      "amount": 30000.00,
      "description": "Accrued salary expense"
    },
    {
      "ledgerId": 2201,  // Accrued Expenses
      "entryType": "CREDIT",
      "amount": 30000.00,
      "description": "Accrued salary payable"
    }
  ]
}
```

---

# 🏦 BANKING OPERATIONS

## Bank Statement Import

### Importing CSV Statement
**Scenario**: Import monthly bank statement

1. **Download statement from bank** (CSV format)
2. **Upload to system**:

```bash
POST /api/v1/banking/statements/import
- bankAccountId: 1002 (HDFC Bank)
- statementDate: 2024-01-31
- file: bank_statement_jan.csv
- importSource: CSV
```

**Expected CSV Format**:
```csv
Date,Description,Debit,Credit,Balance
2024-01-15,Cash Deposit,0,10000,10000
2024-01-16,Vendor Payment,5000,0,5000
2024-01-17,Interest Credit,0,50,5050
2024-01-18,Bank Charges,25,0,5025
```

### Manual Statement Entry
**Scenario**: Enter bank statement manually

```bash
POST /api/v1/banking/statements/manual
- bankAccountId: 1002
- statementDate: 2024-01-31
- openingBalance: 45000.00
- closingBalance: 50000.00
```

## Bank Reconciliation

### Auto-Reconciliation
**Scenario**: Automatically match transactions

```bash
POST /api/v1/banking/statements/1/auto-reconcile
```

**What This Does**:
- Matches bank transactions with your vouchers
- Identifies exact matches by amount and date
- Marks matched transactions as reconciled
- Shows unmatched items for manual review

### Manual Reconciliation
**Scenario**: Manually match a transaction

```bash
POST /api/v1/banking/statements/lines/1/reconcile
- transactionEntryId: 15
- notes: "Manual match - bank charges"
```

### Reconciliation Report
**Scenario**: Review reconciliation status

```bash
GET /api/v1/banking/reconciliation/report?bankAccountId=1002&fromDate=2024-01-01&toDate=2024-01-31
```

**Report Shows**:
- Statement balance vs Book balance
- Reconciled vs Unreconciled transactions
- Outstanding items requiring attention

---

# 📅 MONTH-END PROCEDURES

## Monthly Closing Checklist

### Step 1: Complete All Transactions
- [ ] Record all sales and purchases
- [ ] Process all receipts and payments
- [ ] Enter all cash transactions
- [ ] Record any adjustments

### Step 2: Bank Reconciliation
```bash
# Import bank statements
POST /api/v1/banking/statements/import

# Auto-reconcile
POST /api/v1/banking/statements/{id}/auto-reconcile

# Review and handle unmatched items
GET /api/v1/banking/reconciliation/report
```

### Step 3: Adjustment Entries
```bash
# Record depreciation
POST /api/v1/vouchers/journal (depreciation entry)

# Record accrued expenses
POST /api/v1/vouchers/journal (accrual entry)

# Record prepaid expenses
POST /api/v1/vouchers/journal (prepaid adjustment)
```

### Step 4: Generate Reports
```bash
# Trial Balance
GET /api/v1/reports/financial/trial-balance?fromDate=2024-01-01&toDate=2024-01-31

# Profit & Loss
GET /api/v1/reports/financial/profit-loss?fromDate=2024-01-01&toDate=2024-01-31

# Balance Sheet
GET /api/v1/reports/financial/balance-sheet?asOfDate=2024-01-31
```

### Step 5: Review and Approve
- [ ] Verify trial balance is balanced
- [ ] Review P&L for accuracy
- [ ] Check balance sheet totals
- [ ] Approve all adjustment entries

---

# 📊 FINANCIAL REPORTING

## Trial Balance

### Understanding Trial Balance
The Trial Balance shows all account balances and ensures your books are balanced.

```bash
GET /api/v1/reports/financial/trial-balance?fromDate=2024-01-01&toDate=2024-01-31
```

**What to Look For**:
- Total Debits = Total Credits (must balance)
- All accounts show correct balances
- No missing or duplicate entries

### Sample Trial Balance
```
Account Code | Account Name           | Debit    | Credit
-------------|------------------------|----------|--------
1001        | Cash in Hand           | 5,000    |
1002        | HDFC Bank Current      | 45,000   |
1101        | ABC Company            | 20,000   |
2101        | XYZ Suppliers          |          | 5,000
3001        | Owner's Capital        |          | 100,000
4001        | Sales                  |          | 50,000
5001        | Purchase               | 25,000   |
5201        | Depreciation           | 5,000    |
5301        | Salary                 | 30,000   |
-------------|------------------------|----------|--------
TOTAL       |                        | 130,000  | 130,000
```

## Profit & Loss Statement

### Understanding P&L
Shows your income and expenses for the period.

```bash
GET /api/v1/reports/financial/profit-loss?fromDate=2024-01-01&toDate=2024-01-31
```

### Sample P&L
```
INCOME
Sales Revenue                    | 50,000
Other Income                     | 500
Total Income                     | 50,500

EXPENSES
Purchase                         | 25,000
Depreciation                     | 5,000
Salary                           | 30,000
Other Expenses                   | 2,000
Total Expenses                   | 62,000

NET PROFIT/LOSS                  | (11,500)
```

## Balance Sheet

### Understanding Balance Sheet
Shows your financial position at a specific date.

```bash
GET /api/v1/reports/financial/balance-sheet?asOfDate=2024-01-31
```

### Sample Balance Sheet
```
ASSETS
Current Assets
  Cash in Hand                   | 5,000
  HDFC Bank Current              | 45,000
  ABC Company (Receivable)       | 20,000
Total Current Assets             | 70,000

Fixed Assets
  Plant & Machinery              | 100,000
  Less: Accumulated Depreciation | (5,000)
Net Fixed Assets                 | 95,000

Total Assets                     | 165,000

LIABILITIES
Current Liabilities
  XYZ Suppliers (Payable)        | 5,000
  Accrued Expenses               | 30,000
Total Current Liabilities        | 35,000

EQUITY
Owner's Capital                  | 100,000
Retained Earnings                | 30,000
Total Equity                     | 130,000

Total Liabilities & Equity       | 165,000
```

## Cash Flow Statement

### Understanding Cash Flow
Shows cash movements during the period.

```bash
GET /api/v1/reports/financial/cash-flow?fromDate=2024-01-01&toDate=2024-01-31
```

---

# 🏁 YEAR-END CLOSING

## Annual Closing Process

### Step 1: Complete Year-End Transactions
- [ ] Record all December transactions
- [ ] Complete bank reconciliations
- [ ] Record year-end adjustments
- [ ] Verify all balances

### Step 2: Closing Entries
```bash
# Close Revenue Accounts
POST /api/v1/vouchers/journal
{
  "voucherDate": "2024-12-31",
  "narration": "Year-end closing - Revenue accounts",
  "transactionEntries": [
    {
      "ledgerId": 4001,  // Sales
      "entryType": "DEBIT",
      "amount": 500000.00,
      "description": "Close sales account"
    },
    {
      "ledgerId": 3002,  // Retained Earnings
      "entryType": "CREDIT",
      "amount": 500000.00,
      "description": "Transfer to retained earnings"
    }
  ]
}

# Close Expense Accounts
POST /api/v1/vouchers/journal
{
  "voucherDate": "2024-12-31",
  "narration": "Year-end closing - Expense accounts",
  "transactionEntries": [
    {
      "ledgerId": 3002,  // Retained Earnings
      "entryType": "DEBIT",
      "amount": 400000.00,
      "description": "Transfer expenses to retained earnings"
    },
    {
      "ledgerId": 5001,  // Purchase
      "entryType": "CREDIT",
      "amount": 200000.00,
      "description": "Close purchase account"
    },
    {
      "ledgerId": 5301,  // Salary
      "entryType": "CREDIT",
      "amount": 200000.00,
      "description": "Close salary account"
    }
  ]
}
```

### Step 3: Generate Year-End Reports
- [ ] Annual Trial Balance
- [ ] Annual P&L Statement
- [ ] Year-End Balance Sheet
- [ ] Annual Cash Flow Statement

### Step 4: Archive and Backup
- [ ] Backup all data
- [ ] Archive previous year's data
- [ ] Set up new fiscal year
- [ ] Create opening balances for new year

---

# 🔄 COMMON SCENARIOS

## Scenario 1: Customer Returns Goods

**Situation**: Customer returns goods worth ₹5,000

**Solution**: Create a Credit Note
```bash
POST /api/v1/vouchers/credit-note
{
  "voucherDate": "2024-01-20",
  "customerLedgerId": 1101,  // ABC Company
  "amount": 5000.00,
  "reason": "Return of defective goods",
  "narration": "Credit Note for returned goods",
  "referenceNumber": "CN-001"
}
```

## Scenario 2: Vendor Issues Debit Note

**Situation**: Vendor charges extra ₹1,000 for additional services

**Solution**: Create a Debit Note
```bash
POST /api/v1/vouchers/debit-note
{
  "voucherDate": "2024-01-22",
  "vendorLedgerId": 2101,  // XYZ Suppliers
  "amount": 1000.00,
  "reason": "Additional service charges",
  "narration": "Debit Note for extra charges",
  "referenceNumber": "DN-001"
}
```

## Scenario 3: Bank Charges Not Recorded

**Situation**: Bank charged ₹500 but you didn't record it

**Solution**: Record bank charges
```bash
POST /api/v1/vouchers/journal
{
  "voucherDate": "2024-01-25",
  "narration": "Bank charges for January",
  "transactionEntries": [
    {
      "ledgerId": 5401,  // Bank Charges
      "entryType": "DEBIT",
      "amount": 500.00,
      "description": "Monthly bank charges"
    },
    {
      "ledgerId": 1002,  // HDFC Bank
      "entryType": "CREDIT",
      "amount": 500.00,
      "description": "Bank charges deducted"
    }
  ]
}
```

## Scenario 4: Outstanding Checks

**Situation**: You issued a check for ₹10,000 but it's not yet cashed

**Solution**: 
1. Record the payment when issued
2. During reconciliation, mark the check as outstanding
3. The reconciliation report will show the difference

## Scenario 5: Prepaid Expenses

**Situation**: You paid ₹12,000 for annual insurance in advance

**Solution**: 
1. **When Payment Made**:
```bash
POST /api/v1/vouchers/payment
{
  "narration": "Annual insurance premium paid in advance",
  "transactionEntries": [
    {
      "ledgerId": 1201,  // Prepaid Insurance
      "entryType": "DEBIT",
      "amount": 12000.00,
      "description": "Insurance paid in advance"
    },
    {
      "ledgerId": 1002,  // HDFC Bank
      "entryType": "CREDIT",
      "amount": 12000.00,
      "description": "Payment for insurance"
    }
  ]
}
```

2. **Monthly Adjustment** (₹1,000 per month):
```bash
POST /api/v1/vouchers/journal
{
  "narration": "Monthly insurance expense",
  "transactionEntries": [
    {
      "ledgerId": 5402,  // Insurance Expense
      "entryType": "DEBIT",
      "amount": 1000.00,
      "description": "Monthly insurance expense"
    },
    {
      "ledgerId": 1201,  // Prepaid Insurance
      "entryType": "CREDIT",
      "amount": 1000.00,
      "description": "Prepaid insurance adjustment"
    }
  ]
}
```

---

# 🔧 TROUBLESHOOTING

## Common Issues and Solutions

### Issue 1: Trial Balance Not Balancing
**Problem**: Total debits ≠ Total credits

**Possible Causes**:
- Missing transaction entries
- Wrong entry types (debit/credit)
- Incorrect amounts

**Solution**:
1. Check all recent vouchers
2. Verify entry types and amounts
3. Look for missing entries
4. Use the audit trail to trace discrepancies

### Issue 2: Bank Reconciliation Issues
**Problem**: Bank balance doesn't match book balance

**Possible Causes**:
- Unrecorded bank charges
- Outstanding checks
- Deposits in transit
- Bank errors

**Solution**:
1. Import latest bank statement
2. Check for unrecorded transactions
3. Verify all payments and receipts
4. Contact bank for any discrepancies

### Issue 3: Customer Balance Issues
**Problem**: Customer shows wrong balance

**Possible Causes**:
- Missing receipts
- Duplicate invoices
- Wrong entry types

**Solution**:
1. Check all transactions for the customer
2. Verify receipts against invoices
3. Look for duplicate entries
4. Create adjustment entry if needed

### Issue 4: Report Generation Errors
**Problem**: Reports show incorrect data

**Possible Causes**:
- Wrong date ranges
- Unposted vouchers
- Incorrect account mappings

**Solution**:
1. Verify date ranges
2. Check voucher posting status
3. Review account mappings
4. Regenerate reports

---

# 💡 BEST PRACTICES

## Daily Operations

### 1. Record Transactions Promptly
- Enter transactions as they occur
- Don't wait until month-end
- Keep supporting documents

### 2. Use Proper References
- Always use reference numbers
- Link related transactions
- Maintain clear descriptions

### 3. Regular Reconciliation
- Reconcile bank accounts weekly
- Check customer/vendor balances monthly
- Verify cash accounts daily

## Month-End Procedures

### 1. Complete All Transactions
- Record all pending transactions
- Process all receipts and payments
- Enter all adjustments

### 2. Review and Verify
- Check trial balance
- Verify all reconciliations
- Review P&L and Balance Sheet

### 3. Document Everything
- Keep audit trail
- Maintain supporting documents
- Record all adjustments with explanations

## Year-End Procedures

### 1. Complete Year-End Closing
- Process all December transactions
- Complete all reconciliations
- Record all adjustments

### 2. Generate Final Reports
- Annual financial statements
- Tax reports
- Audit documentation

### 3. Archive and Backup
- Backup all data
- Archive previous year
- Set up new fiscal year

## Security and Compliance

### 1. Access Control
- Limit access to authorized users
- Use strong passwords
- Regular access reviews

### 2. Audit Trail
- Never delete transactions
- Use reversal entries for corrections
- Maintain complete audit trail

### 3. Regular Backups
- Daily automated backups
- Monthly manual backups
- Test restore procedures

---

# 📞 SUPPORT AND HELP

## Getting Help

### API Documentation
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Complete API reference with examples

### Error Messages
- All error messages include detailed explanations
- Check the response for specific error details
- Use the troubleshooting guide above

### Best Practices
- Follow the procedures outlined in this manual
- Use the provided examples as templates
- Maintain proper documentation

## Contact Information

For technical support or questions:
- Check the API documentation first
- Review this user manual
- Use the troubleshooting guide
- Contact your system administrator

---

**Document Version**: 1.0  
**Last Updated**: January 2024  
**For**: Accounting Module Users  
**Next Review**: March 2024
