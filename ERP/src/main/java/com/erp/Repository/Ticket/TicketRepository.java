package com.erp.Repository.Ticket;

import com.erp.Dto.Response.TicketResponseDTO;
import com.erp.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
