package com.erp.Controller.BankAccount;

import com.erp.Dto.Request.BankAccountGetRequest;
import com.erp.Dto.Request.BankAccountRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.BankAccountResponse;
import com.erp.Dto.Response.BankBalanceResponse;
import com.erp.Projection.BankAccountProjection;
import com.erp.Service.BankAccount.BankAccountService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("bank-account/{ledgerId}")
    public ResponseEntity<ResponseStructure<BankAccountResponse>> createBankAccount(@RequestBody BankAccountRequest bankAccountRequest ,@PathVariable long ledgerId){
        BankAccountResponse bankAccountResponse = bankAccountService.createBankAccount(bankAccountRequest , ledgerId);
        return ResponseBuilder.success(HttpStatus.CREATED, "Bank Account Created", bankAccountResponse);
    }

    @PutMapping("bank-account-update")
    public ResponseEntity<ResponseStructure<BankAccountResponse>> updateBankAccount(@RequestBody BankAccountRequest bankAccountRequest){
        BankAccountResponse bankAccountResponse = bankAccountService.updateBankAccount(bankAccountRequest);
        return ResponseBuilder.success(HttpStatus.OK,"Bank Account Updated Successfully", bankAccountResponse);
    }

//    @PostMapping("bank-account-find")
//    public ResponseEntity<ResponseStructure<BankAccountResponse>> findByBankAccountId(@RequestBody CommanParam param){
//        BankAccountResponse bankAccountResponse = bankAccountService.findByBankAccountId(param);
//        return ResponseBuilder.success(HttpStatus.OK,"Bank Account Found Successfully!", bankAccountResponse);
//    }

    @PostMapping("/find")
    public ResponseEntity<ListResponseStructure<BankAccountProjection>> findBankAccount(
            @RequestBody FilterRequest filterRequest) {

        List<BankAccountProjection> projections = bankAccountService.findByBankAccountId(filterRequest);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Bank Account(s) Found Successfully!",
                projections
        );
    }



    @DeleteMapping("bank-account-delete")
    public ResponseEntity<ResponseStructure<BankAccountResponse>> deleteByBankAccountId(@RequestBody BankAccountRequest request){
        BankAccountResponse bankAccountResponse = bankAccountService.deleteByBankAccountId(request);
        return ResponseBuilder.success(HttpStatus.OK,"Bank Account Deleted Successfully!", bankAccountResponse);
    }

    @GetMapping("bank-accounts")
    public ResponseEntity<ListResponseStructure<BankAccountResponse>> getAllBankAccounts(){
        List<BankAccountResponse> allBankAccounts = bankAccountService.getAllBankAccounts();
        return ResponseBuilder.success(HttpStatus.OK,"All Bank Accounts Fetched Successfully", allBankAccounts);
    }

    @GetMapping("/balance")
    public ResponseEntity<ResponseStructure<BankBalanceResponse>> getCurrentBankBalance() {
        BankBalanceResponse response = bankAccountService.getCurrentBankBalance();
        return ResponseBuilder.success(HttpStatus.OK, "Bank balance fetched successfully", response);
    }
}