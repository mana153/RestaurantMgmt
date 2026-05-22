package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.MenuItem;
import com.lodging.restaurant.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    public String getAllMenuItems(Model model) {
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "menu-list";
    }

    @GetMapping("/new")
    public String showMenuItemForm(Model model) {
        model.addAttribute("menuItem", new MenuItem());
        return "menu-form";
    }

    @PostMapping
    public String saveMenuItem(@ModelAttribute MenuItem menuItem) {
        menuItemService.saveMenuItem(menuItem);
        return "redirect:/menu-items";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("menuItem", menuItemService.getMenuItemById(id));
        return "menu-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/menu-items";
    }

    @GetMapping("/toggle-sold-out/{id}")
    public String toggleSoldOut(@PathVariable Long id) {
        MenuItem menuItem = menuItemService.getMenuItemById(id);
        if (menuItem != null) {
            menuItem.setSoldOut(!menuItem.isSoldOut());
            menuItemService.saveMenuItem(menuItem);
        }
        return "redirect:/menu-items";
    }
}
