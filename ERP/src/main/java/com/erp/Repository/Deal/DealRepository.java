package com.erp.Repository.Deal;

import com.erp.Enum.DealStage;
import com.erp.Model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    List<Deal> findByStage(DealStage stage);
    List<Deal> findByAssignedToId(Long assignedToId);
}
