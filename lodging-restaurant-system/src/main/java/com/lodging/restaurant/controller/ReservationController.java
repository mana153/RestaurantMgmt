package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.Reservation;
import com.lodging.restaurant.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public String saveReservation(Reservation reservation) {
        reservationService.saveReservation(reservation);
        return "redirect:/booking";
    }
}
