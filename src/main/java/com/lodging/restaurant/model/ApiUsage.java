package com.lodging.restaurant.model;

import jakarta.persistence.*;

@Entity
public class ApiUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpointName;
    private int hitCount;

    public ApiUsage() {
    }

    public ApiUsage(String endpointName, int hitCount) {
        this.endpointName = endpointName;
        this.hitCount = hitCount;
    }

    public Long getId() {
        return id;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }
}