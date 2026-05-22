package com.lodging.restaurant.repository;

import com.lodging.restaurant.model.HousekeepingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HousekeepingLogRepository extends JpaRepository<HousekeepingLog, Long> {
}
