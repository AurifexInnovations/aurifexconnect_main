package com.erp.Repository;

import com.erp.Model.InternalTicket;

import com.erp.Projection.InternalTicketProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternalTicketRepository extends JpaRepository<InternalTicket, Long> {

    @Query(value = """
        SELECT 
            it.id,
            it.title,
            it.priority,
            it.status,
            it.assigned_to AS assignedTo,
            it.created_by AS createdBy,
            u1.first_name AS createdByName,
            u2.first_name AS assignedToName,
            it.created_date AS createdDate,
            it.last_updated AS lastUpdated
        FROM tenant_1_palak_gmail_com.internal_ticket AS it
        LEFT JOIN tenant_1_palak_gmail_com.users AS u1 ON u1.id = it.created_by
        LEFT JOIN tenant_1_palak_gmail_com.users AS u2 ON u2.id = it.assigned_to
        ORDER BY it.id DESC
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<InternalTicketProjection> findAllTicketsWithUserNames(
            @Param("limit") int limit,
            @Param("offset") int offset
    );


    @Query(value = "SELECT COUNT(*) FROM internal_ticket", nativeQuery = true)
    long getTotalTicketCount();


}
