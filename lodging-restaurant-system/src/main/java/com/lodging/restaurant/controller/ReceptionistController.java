package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.Folio;
import com.lodging.restaurant.model.Reservation;
import com.lodging.restaurant.service.FolioService;
import com.lodging.restaurant.service.ReservationService;
import com.lodging.restaurant.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/receptionist")
public class ReceptionistController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private FolioService folioService;

    @GetMapping("/room-status")
    public String showRoomStatus(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "room-status";
    }

    @GetMapping("/check-in-out")
    public String showCheckInOutForm(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "check-in-out";
    }

    @PostMapping("/check-in")
    public String checkIn(@RequestParam Long reservationId) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation != null) {
            reservation.setStatus("Checked-in");
            reservation.getRoom().setStatus("Occupied");
            reservation.setDigitalKeyCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            reservationService.saveReservation(reservation);
        }
        return "redirect:/receptionist/check-in-out";
    }

    @PostMapping("/check-out")
    public String checkOut(@RequestParam Long reservationId) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation != null) {
            reservation.setStatus("Checked-out");
            reservation.getRoom().setStatus("Dirty");
            reservation.setDigitalKeyCode(null);
            reservationService.saveReservation(reservation);
        }
        return "redirect:/receptionist/check-in-out";
    }

    @GetMapping("/folios")
    public String showFolioManagement(Model model) {
        model.addAttribute("folios", folioService.getAllFolios());
        return "folio-management";
    }

    @PostMapping("/folios/settle")
    public String settleBill(@RequestParam Long folioId) {
        Folio folio = folioService.getFolioById(folioId);
        if (folio != null) {
            folio.setSettled(true);
            folioService.saveFolio(folio);
        }
        return "redirect:/receptionist/folios";
    }
}
