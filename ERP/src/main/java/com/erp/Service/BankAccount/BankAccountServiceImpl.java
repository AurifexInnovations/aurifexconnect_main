package com.erp.Service.BankAccount;

import com.erp.CustomRepository.BankCustomRepository;
import com.erp.Dto.Request.BankAccountGetRequest;
import com.erp.Dto.Request.BankAccountRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.BankAccountResponse;
import com.erp.Dto.Response.BankBalanceResponse;
import com.erp.Exception.BankAccount.BankAccountNotFoundException;
import com.erp.Exception.Ledger.LedgerNotFoundException;
import com.erp.Mapper.BankAccount.BankAccountMapper;
import com.erp.Model.BankAccount;
import com.erp.Model.Ledger;
import com.erp.Projection.BankAccountProjection;
import com.erp.Repository.BankAccount.BankAccountRepository;
import com.erp.Repository.Ledger.LedgerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService{

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final LedgerRepository ledgerRepository;

    private final BankCustomRepository bankCustomRepository;


    @Override
    public BankAccountResponse createBankAccount(BankAccountRequest bankAccountRequest, long ledgerId){
        Ledger ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new LedgerNotFoundException("Ledger not found , invalid ledger Id"));


        BankAccount bankAccount = bankAccountMapper.mapToBankAccount(bankAccountRequest);
        bankAccount.setCurrentBalance(bankAccount.getOpeningBalance());
        bankAccount.setLedger(ledger);
        bankAccountRepository.save(bankAccount);
        return bankAccountMapper.mapToBankAccountResponse(bankAccount);
    }

    @Override
    public BankAccountResponse updateBankAccount(BankAccountRequest bankAccountRequest){
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountRequest.getFindBankAccountId())
                .orElseThrow(()-> new BankAccountNotFoundException("Bank Account Not Found! Invalid Id"));

        bankAccountMapper.mapToBankAccountEntity(bankAccountRequest, bankAccount);
        bankAccountRepository.save(bankAccount);
        return bankAccountMapper.mapToBankAccountResponse(bankAccount);
    }

//    @Override
//    public BankAccountResponse findByBankAccountId(CommanParam bankAccountId){
//        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId.getId())
//                .orElseThrow(()->new BankAccountNotFoundException("Bank Account Not Found! Invalid Id"));
//        return bankAccountMapper.mapToBankAccountResponse(bankAccount);
//    }

    @Override
    public List<BankAccountProjection> findByBankAccountId(FilterRequest filterRequest) {
        if (filterRequest == null) {
            throw new BankAccountNotFoundException("Filter data is required!");
        }

        // Call custom repository to fetch list by filter
        List<BankAccountProjection> projections = bankCustomRepository.getBankAccounts(filterRequest);

        if (projections == null || projections.isEmpty()) {
            throw new BankAccountNotFoundException("Bank Account Not Found! Invalid filter criteria");
        }

        return projections; // Return list of projections
    }

    @Override
    public BankAccountResponse deleteByBankAccountId(BankAccountRequest bankAccountId){
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId.getFindBankAccountId())
                .orElseThrow(()-> new BankAccountNotFoundException("Bank Account Not Found! Invalid Id"));
        bankAccountRepository.deleteById(bankAccount.getBankAccountId());
        return bankAccountMapper.mapToBankAccountResponse(bankAccount);
    }

    @Override
    public List<BankAccountResponse> getAllBankAccounts(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream()
                .map(bankAccountMapper::mapToBankAccountResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BankBalanceResponse getCurrentBankBalance() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        double total = 0.0;

        if (accounts != null && !accounts.isEmpty()) {
            for (BankAccount account : accounts) {
                if (account != null && account.getCurrentBalance() != 0.0) {
                    total += account.getCurrentBalance();
                } else if (account != null) {
                    total += 0.0; // Explicit, even though it's redundant
                }
            }
        } else {
            total = 0.0;
        }

        return new BankBalanceResponse("Total Bank Balance", total);
    }

}