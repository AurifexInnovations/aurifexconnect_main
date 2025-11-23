package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.DebitNoteResponseDTO;
import com.erp.Dto.Response.ResultDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
@Slf4j
public class DebitNoteCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<DebitNoteResponseDTO> getFilteredDebitNotes(FilterRequest filterRequest) {

        StringBuilder sql = new StringBuilder("""
                SELECT 
                    dn.dn_id,
                    dn.bill_id,
                    dn.vendor_id,
                    dn.date_issued,
                    dn.reason,
                    dn.amount_debited,
                    dn.dn_number,
                    dn.inventory_adjustment,
                    dn.tax_adjustment_amount,
                    dn.status
                FROM debit_notes dn
                WHERE dn.is_active = TRUE
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*) FROM debit_notes dn 
                WHERE dn.is_active = TRUE
                """);

        Map<String,String> filters = filterRequest.getFilterColumns();
        Map<String,String> search = filterRequest.getSearchColumns();

        if (filters != null) {
            if (filters.containsKey("dnId")) {
                sql.append(" AND dn.dn_id = :dnId");
                countSql.append(" AND dn.dn_id = :dnId");
            }
            if (filters.containsKey("billId")) {
                sql.append(" AND dn.bill_id = :billId");
                countSql.append(" AND dn.bill_id = :billId");
            }
        }

        if (search != null) {
            if (search.containsKey("reason")) {
                sql.append(" AND LOWER(dn.reason) LIKE :reasonSearch");
                countSql.append(" AND LOWER(dn.reason) LIKE :reasonSearch");
            }
        }

        sql.append(" ORDER BY dn.dn_id DESC");

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        if (filters != null) {
            if (filters.containsKey("dnId")) {
                Long id = Long.parseLong(filters.get("dnId"));
                dataQuery.setParameter("dnId", id);
                countQuery.setParameter("dnId", id);
            }
            if (filters.containsKey("billId")) {
                Long id = Long.parseLong(filters.get("billId"));
                dataQuery.setParameter("billId", id);
                countQuery.setParameter("billId", id);
            }
        }

        if (search != null && search.containsKey("reason")) {
            dataQuery.setParameter("reasonSearch",
                    "%" + search.get("reason").toLowerCase() + "%");
            countQuery.setParameter("reasonSearch",
                    "%" + search.get("reason").toLowerCase() + "%");
        }

        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();

        List<DebitNoteResponseDTO> results = new ArrayList<>();

        for (Object[] r : rows) {
            DebitNoteResponseDTO dto = new DebitNoteResponseDTO();

            dto.setDnId(((Number) r[0]).longValue());
            dto.setBillId(((Number) r[1]).longValue());
            dto.setVendorId(r[2] != null ? ((Number) r[2]).longValue() : null);
            dto.setDateIssued(r[3].toString());
            dto.setReason((String) r[4]);
            dto.setAmountDebited(new BigDecimal(r[5].toString()));
            dto.setDnNumber((String) r[6]);
            dto.setInventoryAdjustment((Boolean) r[7]);
            dto.setTaxAdjustmentAmount(r[8] != null ? new BigDecimal(r[8].toString()) : null);
            dto.setStatus((String) r[9]);

            results.add(dto);
        }

        ResultDto<DebitNoteResponseDTO> result = new ResultDto<>();
        result.setCount(totalCount);
        result.setResults(results);
        return result;
    }
}
