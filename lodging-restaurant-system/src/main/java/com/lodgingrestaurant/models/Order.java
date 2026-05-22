package com.lodgingrestaurant.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber; // Can be null if it is a direct delivery order from Zomato/Swiggy
    private LocalDateTime orderTime;
    private Double totalAmount;
    private String status; // PENDING, PREPARING, DELIVERED, CANCELLED
    private String partnerName; // IN-HOUSE, ZOMATO, SWIGGY

    // Default Constructor
    public Order() {
    }

    // Parameterized Constructor
    public Order(String roomNumber, LocalDateTime orderTime, Double totalAmount, String status, String partnerName) {
        this.roomNumber = roomNumber;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.status = status;
        this.partnerName = partnerName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", orderTime=" + orderTime +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", partnerName='" + partnerName + '\'' +
                '}';
    }
}
