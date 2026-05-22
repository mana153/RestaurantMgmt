package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.Reservation;
import com.lodging.restaurant.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public String showBookingForm(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("reservation", new Reservation());
        return "booking-form";
    }
}
