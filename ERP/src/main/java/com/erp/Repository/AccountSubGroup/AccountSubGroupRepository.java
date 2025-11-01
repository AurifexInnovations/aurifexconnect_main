package com.erp.Repository.AccountSubGroup;

import com.erp.Model.AccountSubGroup;
import com.erp.Model.AccountGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountSubGroupRepository extends JpaRepository<AccountSubGroup, Long> {

    /**
     * Find subgroup by name and group
     */
    Optional<AccountSubGroup> findBySubgroupNameAndAccountGroup(String subgroupName, AccountGroup accountGroup);

    /**
     * Find subgroup by code
     */
    Optional<AccountSubGroup> findBySubgroupCode(String subgroupCode);

    /**
     * Find all active subgroups for a group
     */
    List<AccountSubGroup> findByAccountGroupAndIsActiveTrueOrderBySortOrderAsc(AccountGroup accountGroup);

    /**
     * Find all active subgroups
     */
    List<AccountSubGroup> findByIsActiveTrueOrderBySortOrderAsc();

    /**
     * Check if subgroup name exists within a group (excluding current record for updates)
     */
    @Query("SELECT COUNT(a) > 0 FROM AccountSubGroup a WHERE a.subgroupName = :name AND a.accountGroup = :group AND a.subgroupId != :excludeId")
    boolean existsBySubgroupNameAndGroupAndNotId(@Param("name") String name, @Param("group") AccountGroup group, @Param("excludeId") Long excludeId);

    /**
     * Check if subgroup code exists (excluding current record for updates)
     */
    @Query("SELECT COUNT(a) > 0 FROM AccountSubGroup a WHERE a.subgroupCode = :code AND a.subgroupId != :excludeId")
    boolean existsBySubgroupCodeAndNotId(@Param("code") String code, @Param("excludeId") Long excludeId);

    /**
     * Get next available sort order for a group
     */
    @Query("SELECT COALESCE(MAX(a.sortOrder), 0) + 1 FROM AccountSubGroup a WHERE a.accountGroup = :group")
    Integer getNextSortOrderForGroup(@Param("group") AccountGroup group);
}
