package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.PaginationRequest;
import com.erp.Projection.BankAccountProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class BankCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<BankAccountProjection> getBankAccounts(FilterRequest filterRequest) {
        log.info("Into [BankCustomRepository] [getBankAccounts]");

        try {
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
            Map<String, String> orderBy = filterRequest.getOrderByColumns();

            // ----- BALANCE RANGE FILTERS -----
            if (filters != null) {
                if (filters.get("openingBalanceMin") != null)
                    sql.append(" AND b.opening_balance >= :openingBalanceMin");
                if (filters.get("openingBalanceMax") != null)
                    sql.append(" AND b.opening_balance <= :openingBalanceMax");
                if (filters.get("currentBalanceMin") != null)
                    sql.append(" AND b.current_balance >= :currentBalanceMin");
                if (filters.get("currentBalanceMax") != null)
                    sql.append(" AND b.current_balance <= :currentBalanceMax");
            }

            // ----- ORDER BY -----
            if (orderBy != null && !orderBy.isEmpty()) {
                sql.append(" ORDER BY ");
                List<String> orderClauses = new ArrayList<>();
                orderBy.forEach((column, direction) -> {
                    String dir = direction.equalsIgnoreCase("desc") ? "DESC" : "ASC";
                    switch (column) {
                        case "createdAt" -> orderClauses.add("b.created_at " + dir);
                        case "bankName" -> orderClauses.add("b.bank_name " + dir);
                        case "currentBalance" -> orderClauses.add("b.current_balance " + dir);
                        case "openingBalance" -> orderClauses.add("b.opening_balance " + dir);
                    }
                });
                if (!orderClauses.isEmpty()) sql.append(String.join(", ", orderClauses));
            } else {
                sql.append(" ORDER BY b.created_at DESC");
            }

            Query query = entityManager.createNativeQuery(sql.toString());

            // ----- SET PARAMETERS -----
            if (filters != null) {
                filters.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        switch (key) {
                            case "openingBalanceMin", "openingBalanceMax",
                                 "currentBalanceMin", "currentBalanceMax" ->
                                    query.setParameter(key, Double.parseDouble(value));
                        }
                    }
                });
            }

            // ----- PAGINATION -----
            PaginationRequest pageRequest = filterRequest.getPaginationRequest();
            if (pageRequest != null) {
                int page = pageRequest.getPageNumber() != null ? pageRequest.getPageNumber() : 0;
                int size = pageRequest.getPageSize() != null ? pageRequest.getPageSize() : 10;
                query.setFirstResult(page * size);
                query.setMaxResults(size);
            }

            // ----- EXECUTE QUERY -----
            @SuppressWarnings("unchecked")
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
                dto.setCreatedAt(row[7] != null ? ((java.sql.Timestamp) row[7]).toLocalDateTime() : null);
                result.add(dto);
            }

            log.info("Exit [BankCustomRepository] [getBankAccounts] with count = {}", result.size());
            return result;

        } catch (Exception e) {
            log.error("Exception in [BankCustomRepository] [getBankAccounts]: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch bank accounts", e);
        }
    }
}
