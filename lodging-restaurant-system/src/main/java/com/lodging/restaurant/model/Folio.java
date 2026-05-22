package com.lodging.restaurant.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Folio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Reservation reservation;

    @OneToMany
    private List<MenuItem> orderedItems;

    private Double totalBill;
    private boolean settled;
    private LocalDateTime createdAt;
    private Double discount; // New field for discounts
}
