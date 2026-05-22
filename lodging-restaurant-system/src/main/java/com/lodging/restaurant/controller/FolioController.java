package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.Folio;
import com.lodging.restaurant.model.MenuItem;
import com.lodging.restaurant.service.FolioService;
import com.lodging.restaurant.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/folio")
public class FolioController {

    @Autowired
    private FolioService folioService;

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/{id}")
    public String showFolio(@PathVariable Long id, Model model) {
        model.addAttribute("folio", folioService.getFolioById(id));
        return "folio";
    }

    @PostMapping("/{folioId}/add-item")
    public String addItemToFolio(@PathVariable Long folioId, @RequestParam Long menuItemId) {
        Folio folio = folioService.getFolioById(folioId);
        MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);
        if (folio != null && menuItem != null) {
            folio.getOrderedItems().add(menuItem);
            folio.setTotalBill(folio.getTotalBill() + menuItem.getPrice());
            folioService.saveFolio(folio);
        }
        return "redirect:/folio/" + folioId;
    }

    @PostMapping("/{folioId}/apply-discount")
    public String applyDiscount(@PathVariable Long folioId, @RequestParam Double discount) {
        Folio folio = folioService.getFolioById(folioId);
        if (folio != null) {
            folio.setDiscount(discount);
            folioService.saveFolio(folio);
        }
        return "redirect:/folio/" + folioId;
    }
}
