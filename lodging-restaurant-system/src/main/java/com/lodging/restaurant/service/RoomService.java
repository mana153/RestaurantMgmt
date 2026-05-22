package com.lodging.restaurant.service;

import com.lodging.restaurant.model.Room;
import com.lodging.restaurant.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AuditLogService auditLogService;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public Room saveRoom(Room room) {
        auditLogService.log("Room status changed", "System", "Room " + room.getRoomNumber() + " status changed to " + room.getStatus());
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        auditLogService.log("Room deleted", "System", "Room with id " + id + " deleted");
        roomRepository.deleteById(id);
    }
}
