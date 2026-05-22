package com.lodging.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lodging.restaurant.model.Folio;
import com.lodging.restaurant.model.MenuItem;
import com.lodging.restaurant.model.RestaurantTable;
import com.lodging.restaurant.service.FolioService;
import com.lodging.restaurant.service.MenuItemService;
import com.lodging.restaurant.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cashier")
public class CashierController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private FolioService folioService;

    @Autowired
    private RestaurantTableService restaurantTableService;

    @GetMapping("/pos")
    public String showPosTerminal(Model model) {
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "pos-terminal";
    }

    @PostMapping("/pos/charge-to-room")
    public String chargeToRoom(@RequestParam Long folioId, @RequestParam String orderedItems) {
        Folio folio = folioService.getFolioById(folioId);
        if (folio != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Long> menuItemIds = objectMapper.readValue(orderedItems, List.class);
                
                List<MenuItem> newOrderedItems = new ArrayList<>();
                double newTotal = folio.getTotalBill() != null ? folio.getTotalBill() : 0.0;

                for (Long itemId : menuItemIds) {
                    MenuItem item = menuItemService.getMenuItemById(itemId);
                    if (item != null) {
                        newOrderedItems.add(item);
                        newTotal += item.getPrice();
                    }
                }
                
                if (folio.getOrderedItems() == null) {
                    folio.setOrderedItems(new ArrayList<>());
                }
                folio.getOrderedItems().addAll(newOrderedItems);
                folio.setTotalBill(newTotal);
                folio.setCreatedAt(LocalDateTime.now()); // Set the order timestamp
                folioService.saveFolio(folio);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/cashier/pos";
    }

    @PostMapping("/pos/finalize-bill")
    public String finalizeBill() {
        return "redirect:/cashier/pos";
    }

    @GetMapping("/tables")
    public String showTableManagement(Model model) {
        model.addAttribute("tables", restaurantTableService.getAllTables());
        return "table-management";
    }

    @PostMapping("/tables/toggle-occupied/{id}")
    public String toggleTableOccupied(@PathVariable Long id) {
        RestaurantTable table = restaurantTableService.getTableById(id);
        if (table != null) {
            table.setOccupied(!table.isOccupied());
            restaurantTableService.saveTable(table);
        }
        return "redirect:/cashier/tables";
    }
}
