package com.lodging.restaurant.repository;

import com.lodging.restaurant.model.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {

    ApiUsage findByEndpoint(String endpoint);
}
