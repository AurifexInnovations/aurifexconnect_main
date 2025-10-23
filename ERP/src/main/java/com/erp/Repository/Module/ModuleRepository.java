package com.erp.Repository.Module;

import com.erp.Model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module , Long> {

    @Query(value =  "select * from modules where name = :name and active = true " , nativeQuery = true)
    Module findByName(String name);

    @Query(value =  "select * from modules where id = :id and active = true " , nativeQuery = true)
    Module findById(long id);

    @Query(value =  "select * from modules where active = true " , nativeQuery = true)
    List<Module> getAllModules();
}
