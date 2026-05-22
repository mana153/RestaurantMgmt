package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.Folio;
import com.lodging.restaurant.service.FolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chef")
public class ChefController {

    @Autowired
    private FolioService folioService;

    @GetMapping("/kot")
    public String showKotBoard(Model model) {
        model.addAttribute("folios", folioService.getAllFolios());
        return "kot-board";
    }

    @PostMapping("/kot/complete-order")
    public String completeOrder(@RequestParam Long folioId) {
        Folio folio = folioService.getFolioById(folioId);
        if (folio != null) {
            folio.getOrderedItems().clear(); // For simplicity, we clear the items
            folioService.saveFolio(folio);
        }
        return "redirect:/chef/kot";
    }
}
