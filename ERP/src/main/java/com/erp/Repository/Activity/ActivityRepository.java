package com.erp.Repository.Activity;

import com.erp.Model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity , Long> {
}
