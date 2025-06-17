package com.erp.Repository.Leave;

import com.erp.Enum.LeaveType;
import com.erp.Model.Leave;
import com.erp.Enum.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByUserId(long userId);
    List<Leave> findByStatus(LeaveStatus status);
    List<Leave> findByStartDateBetween(LocalDate start, LocalDate end);
    List<Leave> findByUserIdAndStatusAndLeaveType(Long userId, LeaveStatus status, LeaveType leaveType);

}
