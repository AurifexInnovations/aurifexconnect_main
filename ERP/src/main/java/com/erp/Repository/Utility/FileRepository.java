package com.erp.Repository.Utility;

import com.erp.Model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<File , Long> {

    @Query(value = "select max(sequence) from files where gen_id = :genId and " +
            "   category = :category and active = true" ,nativeQuery = true)
    Integer findByGenIdAndCategoryMaxSequence(long genId , String category);

    @Query(value = "select * from files where gen_id = :genId and " +
            "   category = :category and active = true  order by sequence" ,nativeQuery = true)
    List<File> findByGenIdAndCategoryAndOrderBySequence(long genId , String category);


}
