package com.lodging.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "dashboard/admin";
    }

    @GetMapping("/manager/dashboard")
    public String managerDashboard() {
        return "dashboard/manager";
    }

    @GetMapping("/receptionist/dashboard")
    public String receptionistDashboard() {
        return "dashboard/receptionist";
    }

    @GetMapping("/cashier/dashboard")
    public String cashierDashboard() {
        return "dashboard/cashier";
    }

    @GetMapping("/chef/dashboard")
    public String chefDashboard() {
        return "dashboard/chef";
    }

    @GetMapping("/housekeeping/dashboard")
    public String housekeepingDashboard() {
        return "dashboard/housekeeping";
    }

    @GetMapping("/guest/dashboard")
    public String guestDashboard() {
        return "dashboard/guest";
    }
}
