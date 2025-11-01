package com.erp.Repository.Action;

import com.erp.Model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action , Long> {

    @Query(value = "select * from actions " +
            "where name =:name and active=true" , nativeQuery = true)
    Action findByName(String name);

    @Query(value = "select * from actions where id = :id and active= true" , nativeQuery = true)
    Action findById(long id);

    @Query(value =  "select * from actions where active = true " , nativeQuery = true)
    List<Action> findAll();

    @Query(value = "select * from actions where id in (:ids) and active = true" , nativeQuery = true)
    List<Action> findByIds(List<Long> ids);
}
