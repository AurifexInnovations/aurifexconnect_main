package com.erp.Repository;

import com.erp.Model.CreditNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditNoteRepository extends JpaRepository<CreditNote, Integer> {
    boolean existsByCnNumber(String cnNumber);
}
