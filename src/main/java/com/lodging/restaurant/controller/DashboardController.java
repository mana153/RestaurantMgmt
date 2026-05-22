package com.lodging.restaurant.controller;

import com.lodging.restaurant.repository.ApiUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private ApiUsageRepository repository;

    @GetMapping("/api-dashboard")
    public String dashboard(Model model) {

        model.addAttribute("usageList", repository.findAll());

        return "api-dashboard";
    }
}