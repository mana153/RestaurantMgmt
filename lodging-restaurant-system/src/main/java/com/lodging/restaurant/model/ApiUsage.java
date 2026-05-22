package com.lodging.restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ApiUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpoint;
    private int totalRequests;
    private int successCount;
    private int failureCount;

    public ApiUsage() {
    }

    public ApiUsage(String endpoint, int totalRequests, int successCount, int failureCount) {
        this.endpoint = endpoint;
        this.totalRequests = totalRequests;
        this.successCount = successCount;
        this.failureCount = failureCount;
    }
}
