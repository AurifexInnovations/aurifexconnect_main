package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Projection.BankAccountProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
@Slf4j
public class BankCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    //  Filter keys
    private final Map<String, String> filterParamMap = Map.of(
            "accountStatus", "accountStatus",
            "balanceMin", "balanceMin",
            "balanceMax", "balanceMax",
            "createdBy", "createdBy"
    );

    //  Search keys
    private final Map<String, String> searchParamMap = Map.of(
            "accountNumberSearch", "accountNumber",
            "bankNameSearch", "bankName"
    );

    public List<BankAccountProjection> getBankAccounts(FilterRequest filterRequest) {
        log.info("Into [BankCustomRepository] [getBankAccounts]");

        StringBuilder sql = new StringBuilder("""
                SELECT 
                    b.bank_account_id,
                    b.account_number,
                    b.bank_name,
                    b.opening_balance,
                    b.current_balance,
                    b.account_status,
                    b.created_by,
                    b.created_at
                FROM bank_account b
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        //  Apply Filters
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("accountStatus"))
                sql.append(" AND b.account_status = :accountStatus");

            if (filters.containsKey("createdBy"))
                sql.append(" AND LOWER(b.created_by) = LOWER(:createdBy)");

            if (filters.containsKey("balanceMin") && filters.containsKey("balanceMax"))
                sql.append(" AND b.current_balance BETWEEN :balanceMin AND :balanceMax");
            else if (filters.containsKey("balanceMin"))
                sql.append(" AND b.current_balance >= :balanceMin");
            else if (filters.containsKey("balanceMax"))
                sql.append(" AND b.current_balance <= :balanceMax");
        }

        //  Apply Search (LIKE)
        if (search != null && !search.isEmpty()) {
            if (search.containsKey("accountNumberSearch"))
                sql.append(" AND LOWER(b.account_number) LIKE LOWER(CONCAT('%', :accountNumberSearch, '%'))");
            if (search.containsKey("bankNameSearch"))
                sql.append(" AND LOWER(b.bank_name) LIKE LOWER(CONCAT('%', :bankNameSearch, '%'))");
        }

        //  Sorting
        if (orderBy != null && !orderBy.isEmpty()) {
            if (orderBy.containsKey("createdAt")) {
                sql.append(" ORDER BY b.created_at ")
                        .append(orderBy.get("createdAt").equalsIgnoreCase("desc") ? "DESC" : "ASC");
            } else if (orderBy.containsKey("bankName")) {
                sql.append(" ORDER BY b.bank_name ")
                        .append(orderBy.get("bankName").equalsIgnoreCase("desc") ? "DESC" : "ASC");
            }
        } else {
            sql.append(" ORDER BY b.created_at DESC"); // Default sort: newest first
        }

        log.info("[BankCustomRepository] Final Query :: {}", sql);

        Query query = entityManager.createNativeQuery(sql.toString());

        //  Bind Filter Parameters
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "balanceMin", "balanceMax" -> query.setParameter(key, Double.parseDouble(value));
                        default -> query.setParameter(key, value);
                    }
                }
            });
        }

        //  Bind Search Parameters
        if (search != null) {
            search.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    query.setParameter(key, value);
                }
            });
        }

        //  Pagination
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            query.setFirstResult(page * size);
            query.setMaxResults(size);
        }

        //  Map results to Projection
        List<Object[]> rows = query.getResultList();
        List<BankAccountProjection> result = new ArrayList<>();

        for (Object[] row : rows) {
            BankAccountProjection dto = new BankAccountProjection();
            dto.setBankAccountId(((Number) row[0]).longValue());
            dto.setAccountNumber((String) row[1]);
            dto.setBankName((String) row[2]);
            dto.setOpeningBalance(row[3] != null ? ((Number) row[3]).doubleValue() : 0.0);
            dto.setCurrentBalance(row[4] != null ? ((Number) row[4]).doubleValue() : 0.0);
            dto.setAccountStatus((String) row[5]);
            dto.setCreatedBy((String) row[6]);
            dto.setCreatedAt(row[7] != null ? ((Timestamp) row[7]).toLocalDateTime() : null);
            result.add(dto);
        }

        log.info("Exit [BankCustomRepository] [getBankAccounts]");
        return result;
    }
}
