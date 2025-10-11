package com.erp.Repository.Ticket;

import com.erp.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findByPriority(String priority);

    List<Ticket> findByStatus(String status);

    List<Ticket> findByCustomerLocation(String customerLocation);

    List<Ticket> findByTechnicianId(Long technicianId);

    List<Ticket> findByCustomerId(Long customerId);

    @Query("SELECT t FROM Ticket t WHERE " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:customerLocation IS NULL OR t.customerLocation = :customerLocation)")
    List<Ticket> findByFilters(String priority, String status, String customerLocation);
}
