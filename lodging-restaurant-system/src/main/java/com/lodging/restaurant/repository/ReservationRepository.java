package com.lodging.restaurant.repository;

import com.lodging.restaurant.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
