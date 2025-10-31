package com.erp.Repository.AccountGroup;

import com.erp.Model.AccountGroup;
import com.erp.Enum.GroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountGroupRepository extends JpaRepository<AccountGroup, Long> {

    /**
     * Find account group by name
     */
    Optional<AccountGroup> findByGroupName(String groupName);

    /**
     * Find account group by code
     */
    Optional<AccountGroup> findByGroupCode(String groupCode);

    /**
     * Find all active account groups
     */
    List<AccountGroup> findByIsActiveTrueOrderBySortOrderAsc();

    /**
     * Find account groups by type
     */
    List<AccountGroup> findByGroupTypeAndIsActiveTrueOrderBySortOrderAsc(GroupType groupType);

    /**
     * Check if group name exists (excluding current record for updates)
     */
    @Query("SELECT COUNT(a) > 0 FROM AccountGroup a WHERE a.groupName = :name AND a.groupId != :excludeId")
    boolean existsByGroupNameAndNotId(@Param("name") String name, @Param("excludeId") Long excludeId);

    /**
     * Check if group code exists (excluding current record for updates)
     */
    @Query("SELECT COUNT(a) > 0 FROM AccountGroup a WHERE a.groupCode = :code AND a.groupId != :excludeId")
    boolean existsByGroupCodeAndNotId(@Param("code") String code, @Param("excludeId") Long excludeId);

    /**
     * Get next available sort order
     */
    @Query("SELECT COALESCE(MAX(a.sortOrder), 0) + 1 FROM AccountGroup a")
    Integer getNextSortOrder();
}
