package com.lodging.restaurant.repository;

import com.lodging.restaurant.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
}
