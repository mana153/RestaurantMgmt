package com.lodgingrestaurant.repositories;

import com.lodgingrestaurant.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByIsAvailable(boolean isAvailable);
}
