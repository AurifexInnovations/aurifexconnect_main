package com.erp.Repository.Ticket;

import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Dto.Response.TicketViewDTO;
import com.erp.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    @Query("""
    SELECT new com.erp.Dto.Response.TicketViewDTO(
        t.id,
        CONCAT(u.firstName, ' ', u.lastName),

        CONCAT(
            COALESCE(c.city, ''),
            CASE 
                WHEN c.state IS NOT NULL AND c.state <> '' 
                THEN CONCAT(', ', c.state)
                ELSE ''
            END,
            CASE 
                WHEN c.pincode IS NOT NULL AND c.pincode <> '' 
                THEN CONCAT(' - ', c.pincode)
                ELSE ''
            END
        ),

        tk.taskName,
        c.customerName,
        CONCAT(c.email, ' / ', c.phone),
        t.issueDescription,
        t.priority,
        t.ticketStatus,
        t.createdAt,
        t.resolvedAt
    )
    FROM Ticket t
    JOIN Task tk ON tk.taskId = t.taskId
    JOIN User u ON u.id = t.technicianId
    JOIN CustomerDetails c ON c.id = t.customerId
    ORDER BY t.createdAt DESC
""")
    List<TicketViewDTO> findAllTicketViews();
}
