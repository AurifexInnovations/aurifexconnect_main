package com.erp.Repository;

import com.erp.Model.LineItemTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemTaxRepository extends JpaRepository<LineItemTax, Long> {
}
