package com.erp.Repository.Subscribe;

import com.erp.Dto.Response.UserSubscriptionResponse;
import com.erp.Model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByUserId(String userId);
    // You can define custom query methods here if needed
}
