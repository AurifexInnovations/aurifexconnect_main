package com.erp.Repository.Varient;

import com.erp.Model.Varient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface VarientRepository extends JpaRepository<Varient , Long> {

    @Query(value = "select * from varients " +
            "   where id in (:varientIds) " , nativeQuery = true)
    List<Varient> findByIds(Set<Long> varientIds);
}
