package com.lodging.restaurant.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class HousekeepingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Room room;

    @ManyToOne
    private Staff assignedTo;

    private String status; // e.g., Dirty, Clean, Maintenance
    private LocalDateTime timestamp;
}
