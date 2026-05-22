package com.lodgingrestaurant.controllers;

import com.lodgingrestaurant.models.MenuItem;
import com.lodgingrestaurant.services.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // GET /menu - List all menu items (support optional category query param)
    @GetMapping
    public String listMenuItems(@RequestParam(value = "category", required = false) String category, Model model) {
        List<MenuItem> items;
        if (category != null && !category.isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            items = menuItemService.getMenuItemsByCategory(category);
            model.addAttribute("selectedCategory", category.toUpperCase());
        } else {
            items = menuItemService.getAllMenuItems();
            model.addAttribute("selectedCategory", "ALL");
        }
        model.addAttribute("menuItems", items);
        model.addAttribute("pageTitle", "Luxury Fine Dining Menu");
        return "menu/menu-list";
    }

    // GET /menu/new - Form to create a new item
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("menuItem", new MenuItem());
        model.addAttribute("formAction", "/menu");
        model.addAttribute("pageTitle", "Add Exquisite Menu Item");
        return "menu/menu-form";
    }

    // POST /menu - Create new menu item
    @PostMapping
    public String createMenuItem(@ModelAttribute("menuItem") MenuItem menuItem) {
        menuItemService.saveMenuItem(menuItem);
        return "redirect:/menu";
    }

    // GET /menu/edit/{id} - Form to edit existing item
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        MenuItem item = menuItemService.getMenuItemById(id);
        if (item == null) {
            return "redirect:/menu";
        }
        model.addAttribute("menuItem", item);
        model.addAttribute("formAction", "/menu/" + id);
        model.addAttribute("pageTitle", "Refine Masterpiece Item");
        return "menu/menu-form";
    }

    // POST /menu/{id} - Update menu item
    @PostMapping("/{id}")
    public String updateMenuItem(@PathVariable("id") Long id, @ModelAttribute("menuItem") MenuItem menuItem) {
        menuItem.setId(id);
        menuItemService.saveMenuItem(menuItem);
        return "redirect:/menu";
    }

    // GET /menu/delete/{id} - Delete menu item (easily triggered from web hyperlinks)
    @GetMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable("id") Long id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/menu";
    }
}
