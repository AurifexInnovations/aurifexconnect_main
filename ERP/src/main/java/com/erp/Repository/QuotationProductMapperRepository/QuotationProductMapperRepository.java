package com.erp.Repository.QuotationProductMapperRepository;

import com.erp.Model.QuotationProduc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationProductMapperRepository extends JpaRepository<QuotationProduc, Long> {

    List<QuotationProduc> findByQuotationId(Long quotationId);

}
