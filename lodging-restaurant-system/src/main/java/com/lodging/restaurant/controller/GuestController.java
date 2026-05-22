package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.Guest;
import com.lodging.restaurant.service.GuestService;
import com.lodging.restaurant.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private GuestService guestService;

    @GetMapping("/menu")
    public String showMenu(Model model) {
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "guest-menu";
    }

    @GetMapping("/{guestId}/preferences")
    public String showPreferenceManager(@PathVariable Long guestId, Model model) {
        model.addAttribute("guest", guestService.getGuestById(guestId));
        return "preference-manager";
    }

    @PostMapping("/preferences")
    public String savePreferences(@ModelAttribute Guest guest) {
        guestService.saveGuest(guest);
        return "redirect:/guest/" + guest.getId() + "/preferences";
    }
}
