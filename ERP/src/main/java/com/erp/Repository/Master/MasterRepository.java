package com.erp.Repository.Master;

import com.erp.Enum.VoucherType;
import com.erp.Model.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MasterRepository extends JpaRepository<Master, Long> {

    List<Master> findByReferenceMaster(Master invoice);

    List<Master> findByVoucherTypeInAndCreatedDateIsNotNull(List<VoucherType> voucherTypes);

}