package com.lodgingrestaurant.controllers;

import com.lodgingrestaurant.models.Guest;
import com.lodgingrestaurant.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/guest")
public class GuestController {

    private final GuestService guestService;

    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public String listGuests(Model model) {
        model.addAttribute("guests", guestService.getAllGuests());
        model.addAttribute("pageTitle", "Royal Guest Directory");
        return "guest/guest-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("guest", new Guest());
        model.addAttribute("formAction", "/guest");
        model.addAttribute("pageTitle", "Register New VIP Guest");
        return "guest/guest-form";
    }

    @PostMapping
    public String createGuest(@ModelAttribute("guest") Guest guest) {
        guestService.saveGuest(guest);
        return "redirect:/guest";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Guest guest = guestService.getGuestById(id);
        if (guest == null) {
            return "redirect:/guest";
        }
        model.addAttribute("guest", guest);
        model.addAttribute("formAction", "/guest/" + id);
        model.addAttribute("pageTitle", "Refine Guest Registry");
        return "guest/guest-form";
    }

    @PostMapping("/{id}")
    public String updateGuest(@PathVariable("id") Long id, @ModelAttribute("guest") Guest guest) {
        guest.setId(id);
        guestService.saveGuest(guest);
        return "redirect:/guest";
    }

    @GetMapping("/delete/{id}")
    public String deleteGuest(@PathVariable("id") Long id) {
        guestService.deleteGuest(id);
        return "redirect:/guest";
    }
}
