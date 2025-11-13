package com.erp.Repository.followup;

import com.erp.Model.FollowUpDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowUpDetailsRepository extends JpaRepository<FollowUpDetails, Long> {
}
