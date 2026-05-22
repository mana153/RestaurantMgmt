package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.InventoryItem;
import com.lodging.restaurant.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryItemService inventoryItemService;

    @GetMapping
    public String showInventoryList(Model model) {
        model.addAttribute("inventoryItems", inventoryItemService.getAllInventoryItems());
        return "inventory-list";
    }

    @GetMapping("/new")
    public String showInventoryForm(Model model) {
        model.addAttribute("inventoryItem", new InventoryItem());
        return "inventory-form";
    }

    @PostMapping
    public String saveInventoryItem(@ModelAttribute InventoryItem inventoryItem) {
        inventoryItemService.saveInventoryItem(inventoryItem);
        return "redirect:/inventory";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("inventoryItem", inventoryItemService.getInventoryItemById(id));
        return "inventory-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteInventoryItem(@PathVariable Long id) {
        inventoryItemService.deleteInventoryItem(id);
        return "redirect:/inventory";
    }
}
