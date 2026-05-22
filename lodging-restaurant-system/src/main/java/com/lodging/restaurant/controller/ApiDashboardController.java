package com.lodging.restaurant.controller;

import com.lodging.restaurant.repository.ApiUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiDashboardController {

    @Autowired
    private ApiUsageRepository apiUsageRepository;

    @GetMapping("/api/dashboard")
    public String getDashboard(Model model) {
        model.addAttribute("usageStats", apiUsageRepository.findAll());
        return "dashboard";
    }
}
