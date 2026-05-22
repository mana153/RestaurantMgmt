package com.lodging.restaurant.model;

import jakarta.persistence.*;

@Entity
public class PartnerService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String serviceType;
    private String status;

    public PartnerService() {
    }

    public PartnerService(String customerName, String serviceType, String status) {
        this.customerName = customerName;
        this.serviceType = serviceType;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}