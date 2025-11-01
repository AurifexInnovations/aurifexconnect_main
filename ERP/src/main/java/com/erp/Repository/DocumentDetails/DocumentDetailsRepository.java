package com.erp.Repository.DocumentDetails;

import com.erp.Model.DocumentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDetailsRepository extends JpaRepository<DocumentDetails, Long> {
}
