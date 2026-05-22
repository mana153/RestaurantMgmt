package com.lodging.restaurant.repository;

import com.lodging.restaurant.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
