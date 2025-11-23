package com.erp.Repository;

import com.erp.Model.ChartOfAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts, Integer> {
    boolean existsByAccountNumber(String accountNumber);

}
