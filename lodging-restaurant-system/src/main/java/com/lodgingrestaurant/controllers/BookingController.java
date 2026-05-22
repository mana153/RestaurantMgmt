package com.lodgingrestaurant.controllers;

import com.lodgingrestaurant.models.Booking;
import com.lodgingrestaurant.repositories.RoomRepository;
import com.lodgingrestaurant.services.BookingService;
import com.lodgingrestaurant.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;
    private final GuestService guestService;
    private final RoomRepository roomRepository;

    @Autowired
    public BookingController(BookingService bookingService, GuestService guestService, RoomRepository roomRepository) {
        this.bookingService = bookingService;
        this.guestService = guestService;
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("pageTitle", "Imperial Suite Reservations");
        return "booking/booking-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Booking booking = new Booking();
        booking.setStatus("CONFIRMED"); // Default status

        model.addAttribute("booking", booking);
        model.addAttribute("guests", guestService.getAllGuests());
        model.addAttribute("rooms", roomRepository.findAll()); // Allow selecting any room
        model.addAttribute("formAction", "/booking");
        model.addAttribute("pageTitle", "Secure Luxury Reservation");
        return "booking/booking-form";
    }

    @PostMapping
    public String createBooking(@ModelAttribute("booking") Booking booking) {
        bookingService.saveBooking(booking);
        return "redirect:/booking";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return "redirect:/booking";
        }
        model.addAttribute("booking", booking);
        model.addAttribute("guests", guestService.getAllGuests());
        model.addAttribute("rooms", roomRepository.findAll());
        model.addAttribute("formAction", "/booking/" + id);
        model.addAttribute("pageTitle", "Update Royal Booking");
        return "booking/booking-form";
    }

    @PostMapping("/{id}")
    public String updateBooking(@PathVariable("id") Long id, @ModelAttribute("booking") Booking booking) {
        booking.setId(id);
        bookingService.saveBooking(booking);
        return "redirect:/booking";
    }

    // Toggle checked-in/out status or cancel directly via links
    @GetMapping("/status/{id}")
    public String updateStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        bookingService.updateBookingStatus(id, status);
        return "redirect:/booking";
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable("id") Long id) {
        bookingService.deleteBooking(id);
        return "redirect:/booking";
    }
}
