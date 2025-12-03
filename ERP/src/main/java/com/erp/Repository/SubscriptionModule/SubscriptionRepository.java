package com.erp.Repository.SubscriptionModule;

import com.erp.Model.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    Optional<SubscriptionEntity> findBySubscriptionId(Long subscriptionId);

    Optional<SubscriptionEntity> findByUserId(String userId);

    Optional<SubscriptionEntity> findByUserIdAndActiveYn(String userId, String activeYn);

    boolean existsByUserId(String userId);

    List<SubscriptionEntity> findAllByUserIdOrderByCreatedAtAsc(String userId);

}
