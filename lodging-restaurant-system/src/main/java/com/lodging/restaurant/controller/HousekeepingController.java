package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.HousekeepingLog;
import com.lodging.restaurant.model.Room;
import com.lodging.restaurant.model.Staff;
import com.lodging.restaurant.service.HousekeepingLogService;
import com.lodging.restaurant.service.RoomService;
import com.lodging.restaurant.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/housekeeping")
public class HousekeepingController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private HousekeepingLogService housekeepingLogService;

    @GetMapping("/assignments")
    public String showAssignments(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("staff", staffService.getAllStaff());
        return "housekeeping-assignments";
    }

    @PostMapping("/assign")
    public String assign(@RequestParam Long roomId, @RequestParam Long staffId) {
        Room room = roomService.getRoomById(roomId);
        Staff staff = staffService.getStaffById(staffId);
        if (room != null && staff != null) {
            HousekeepingLog log = new HousekeepingLog();
            log.setRoom(room);
            log.setAssignedTo(staff);
            log.setStatus("Assigned");
            log.setTimestamp(LocalDateTime.now());
            housekeepingLogService.saveHousekeepingLog(log);
        }
        return "redirect:/housekeeping/assignments";
    }

    @GetMapping("/my-tasks/{staffId}")
    public String showMyTasks(@PathVariable Long staffId, Model model) {
        model.addAttribute("logs", housekeepingLogService.getAllHousekeepingLogs().stream()
                .filter(log -> log.getAssignedTo().getId().equals(staffId)).toList());
        return "my-tasks";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long logId) {
        HousekeepingLog log = housekeepingLogService.getHousekeepingLogById(logId);
        if (log != null) {
            log.setStatus("Clean");
            log.setTimestamp(LocalDateTime.now());
            log.getRoom().setStatus("Vacant");
            housekeepingLogService.saveHousekeepingLog(log);
        }
        return "redirect:/housekeeping/my-tasks/" + log.getAssignedTo().getId();
    }

    @PostMapping("/maintenance")
    public String markForMaintenance(@RequestParam Long roomId) {
        Room room = roomService.getRoomById(roomId);
        if (room != null) {
            room.setStatus("Out of Order");
            roomService.saveRoom(room);
        }
        return "redirect:/receptionist/room-status";
    }
}
