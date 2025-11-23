package com.erp.Repository.DebitNote;

import com.erp.Model.DebitNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DebitNoteRepository extends JpaRepository<DebitNote, Long> {

    Optional<DebitNote> findByDnIdAndIsActiveTrue(Long id);

}
