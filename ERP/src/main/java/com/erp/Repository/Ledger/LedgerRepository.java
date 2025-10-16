package com.erp.Repository.Ledger;

import com.erp.Model.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerRepository extends JpaRepository<Ledger, Long>{

    List<Ledger> findByLedgerIdOrName(long id, String ledgerName);
}
