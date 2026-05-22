package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.Room;
import com.lodging.restaurant.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/rooms")
public class AdminRoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public String showRoomList(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "room-list";
    }

    @GetMapping("/new")
    public String showRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "room-form";
    }

    @PostMapping
    public String saveRoom(@ModelAttribute Room room) {
        roomService.saveRoom(room);
        return "redirect:/admin/rooms";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.getRoomById(id));
        return "room-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/admin/rooms";
    }
}
