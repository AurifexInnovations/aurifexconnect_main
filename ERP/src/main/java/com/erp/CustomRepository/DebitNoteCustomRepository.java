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
                    dn.dn_id,                 -- 0
                    dn.bill_id,               -- 1
                    dn.vendor_id,             -- 2
                    dn.date_issued,           -- 3
                    dn.reason,                -- 4
                    dn.amount_debited,        -- 5
                    dn.dn_number,             -- 6
                    dn.inventory_adjustment,  -- 7
                    dn.tax_adjustment_amount, -- 8
                    dn.status,                -- 9
                    v.vendor_name             -- 10   <-- NEW
                FROM debit_notes dn
                JOIN vendors v ON v.vendor_id = dn.vendor_id
                WHERE dn.is_active = TRUE
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*) 
                FROM debit_notes dn
                JOIN vendors v ON v.vendor_id = dn.vendor_id
                WHERE dn.is_active = TRUE
                """);

        Map<String,String> filters = filterRequest.getFilterColumns();
        Map<String,String> search = filterRequest.getSearchColumns();

        // ---------------- FILTERS ----------------
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

        // ---------------- SEARCH ----------------
        if (search != null) {

            if (search.containsKey("reason")) {
                sql.append(" AND LOWER(dn.reason) LIKE :reasonSearch");
                countSql.append(" AND LOWER(dn.reason) LIKE :reasonSearch");
            }

            if (search.containsKey("vendorName")) {
                sql.append(" AND LOWER(v.vendor_name) LIKE :vendorNameSearch");
                countSql.append(" AND LOWER(v.vendor_name) LIKE :vendorNameSearch");
            }
        }

        sql.append(" ORDER BY dn.dn_id DESC");

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ---------------- SET FILTER PARAMS ----------------
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

        // ---------------- SET SEARCH PARAMS ----------------
        if (search != null) {

            if (search.containsKey("reason")) {
                dataQuery.setParameter("reasonSearch",
                        "%" + search.get("reason").toLowerCase() + "%");
                countQuery.setParameter("reasonSearch",
                        "%" + search.get("reason").toLowerCase() + "%");
            }

            if (search.containsKey("vendorName")) {
                dataQuery.setParameter("vendorNameSearch",
                        "%" + search.get("vendorName").toLowerCase() + "%");
                countQuery.setParameter("vendorNameSearch",
                        "%" + search.get("vendorName").toLowerCase() + "%");
            }
        }

        // ---------------- PAGINATION ----------------
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
            dto.setVendorName((String) r[10]);

            results.add(dto);
        }

        ResultDto<DebitNoteResponseDTO> result = new ResultDto<>();
        result.setCount(totalCount);
        result.setResults(results);
        return result;
    }
}
