package com.erp.Repository.Vendor;

import com.erp.Model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Query("SELECT v FROM Vendor v WHERE v.emailAddress = :email AND v.isActive = true")
    Optional<Vendor> findActiveVendorByEmail(@Param("email") String email);

    @Query("SELECT v FROM Vendor v WHERE v.vendorId = :id AND v.isActive = true")
    Optional<Vendor> findActiveVendorById(@Param("id") Long id);


}
