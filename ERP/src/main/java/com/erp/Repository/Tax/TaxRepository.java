package com.erp.Repository.Tax;

import com.erp.Enum.TaxName;
import com.erp.Model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {

    Optional<Tax> findByTaxName(TaxName taxName);
    List<Tax> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}