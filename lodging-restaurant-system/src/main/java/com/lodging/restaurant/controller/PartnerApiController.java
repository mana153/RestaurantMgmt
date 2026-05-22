package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.MenuItem;
import com.lodging.restaurant.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerApiController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/listings")
    public List<MenuItem> getListings() {
        return menuItemService.getAllMenuItems();
    }

    @PostMapping("/create")
    public MenuItem create(@RequestBody MenuItem menuItem) {
        return menuItemService.saveMenuItem(menuItem);
    }

    @PutMapping("/update/{id}")
    public MenuItem update(@PathVariable Long id, @RequestBody MenuItem menuItem) {
        // For simplicity, we're just saving the new menu item.
        // In a real application, you'd want to ensure the ID is handled correctly.
        return menuItemService.saveMenuItem(menuItem);
    }

    @DeleteMapping("/cancel/{id}")
    public void cancel(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
    }
}
