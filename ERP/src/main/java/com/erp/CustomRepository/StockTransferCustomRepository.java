package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.StockTransferResponse;
import com.erp.Enum.StockTransferStatus;
import com.erp.Mapper.StockTransfer.StockTransferMapper;
import com.erp.Model.StockTransfer;
import com.erp.Model.Branch;
import com.erp.Model.Inventory;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class StockTransferCustomRepository {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private StockTransferMapper stockTransferMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<StockTransferResponse> getStockTransferDetails(FilterRequest filterRequest) {
        log.info("Into [StockTransferCustomRepository] [getStockTransferDetails]");

        StringBuilder jpql = new StringBuilder("SELECT s FROM StockTransfer s WHERE 1=1 ");

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderby = filterRequest.getOrderByColumns();

        // ---- FILTERS ----
        if (filters != null) {
            if (filters.containsKey("status")) {
                jpql.append(" AND s.status = :status");
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                jpql.append(" AND s.createdAt BETWEEN :startDate AND :endDate");
            }

            if (filters.containsKey("fromBranchId")) {
                jpql.append(" AND s.fromBranch.branchId = :fromBranchId");
            }

            if (filters.containsKey("toBranchId")) {
                jpql.append(" AND s.toBranch.branchId = :toBranchId");
            }
        }

        // ---- SEARCH ----
        if (search != null && search.containsKey("approverName")) {
            jpql.append(" AND s.approvedBy LIKE :approverName");
        }

        // ---- ORDER BY ----
        if (orderby != null && !orderby.isEmpty()) {
            jpql.append(" ORDER BY ");
            List<String> orderClause = new ArrayList<>();
            orderby.forEach((column, direction) -> {
                switch (column) {
                    case "quantity" -> orderClause.add("s.quantity " + ("desc".equalsIgnoreCase(direction) ? "DESC" : "ASC"));
                }
            });
            jpql.append(String.join(", ", orderClause));
        }

        TypedQuery<StockTransfer> query = entityManager.createQuery(jpql.toString(), StockTransfer.class);

        // ---- Set Parameters for Filter ----
        if (filters != null) {
            if (filters.containsKey("status"))
            {
                query.setParameter("status", StockTransferStatus.valueOf(filters.get("status")));
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                query.setParameter("startDate", LocalDateTime.parse(filters.get("startDate")));
                query.setParameter("endDate", LocalDateTime.parse(filters.get("endDate")));
            }
        }

        // ---- Set Parameter for Search ----
        if (search != null && search.containsKey("approverName")) {
            query.setParameter("approverName", "%" + search.get("approverName") + "%");
        }

        // ---- PAGINATION ----
        if (filterRequest.getPaginationRequest() != null) {
            int pageNumber = filterRequest.getPaginationRequest().getPageNumber();
            int pageSize = filterRequest.getPaginationRequest().getPageSize();
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
        }

        List<StockTransfer> transfers = query.getResultList();

        List<StockTransferResponse> responses = new ArrayList<>();
        for (StockTransfer transfer : transfers) {
            StockTransferResponse str = new StockTransferResponse();
            Branch fromBranch = transfer.getFromBranch();
            Branch toBranch = transfer.getToBranch();
            Inventory inventory = transfer.getInventory();

            str.setId(transfer.getId());
            str.setFromBranchName(fromBranch.getBranchName());
            str.setToBranchName(toBranch.getBranchName());
            str.setItemName(inventory.getItemName());
            str.setQuantity(transfer.getQuantity());
            str.setStatus(transfer.getStatus());
            str.setApprovedBy(transfer.getApprovedBy());
            str.setCreatedAt(transfer.getCreatedAt() != null ? transfer.getCreatedAt() : LocalDateTime.now());

            responses.add(str);
        }

        // ---- COUNT QUERY ----
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(s) FROM StockTransfer s WHERE 1=1 ");
        if (filters != null) {
            if (filters.containsKey("status")) {
                countJpql.append(" AND s.status = :status");
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                countJpql.append(" AND s.createdAt BETWEEN :startDate AND :endDate");
            }

            if (filters.containsKey("fromBranchId")) {
                countJpql.append(" AND s.fromBranch.branchId = :fromBranchId");
            }

            if (filters.containsKey("toBranchId")) {
                countJpql.append(" AND s.toBranch.branchId = :toBranchId");
            }


        }
        if (search != null && search.containsKey("approverName")) {
            countJpql.append(" AND s.approvedBy LIKE :approverName");
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);

        if (filters != null) {
            if (filters.containsKey("status")) {
                countQuery.setParameter("status", StockTransferStatus.valueOf(filters.get("status")));
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                countQuery.setParameter("startDate", LocalDateTime.parse(filters.get("startDate")));
                countQuery.setParameter("endDate", LocalDateTime.parse(filters.get("endDate")));
            }
        }
        if (search != null && search.containsKey("approverName")) {
            countQuery.setParameter("approverName", "%" + search.get("approverName") + "%");
        }

        long totalCount = countQuery.getSingleResult();

        ResultDto<StockTransferResponse> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(responses);

        log.info("Exit [StockTransferCustomRepositoryJPQL] with count = {}", totalCount);
        return resultDto;
    }
}
