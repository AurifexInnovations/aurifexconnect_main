package com.erp.Repository.Service;

import com.erp.Model.ServiceDocuments;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceDocumentsRepository  extends JpaRepository<ServiceDocuments, Long> {
    @Query("SELECT s.documentUrl FROM ServiceDocuments s WHERE s.service.serviceId = :serviceId")
    List<String> findAllUrlsByServiceId(@Param("serviceId") Long serviceId);
}
