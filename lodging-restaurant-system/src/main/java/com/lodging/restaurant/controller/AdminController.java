package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.Staff;
import com.lodging.restaurant.service.AuditLogService;
import com.lodging.restaurant.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/users")
    public String showUserManagement(Model model) {
        model.addAttribute("staff", staffService.getAllStaff());
        return "user-management";
    }

    @GetMapping("/users/new")
    public String showStaffForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "staff-form";
    }

    @PostMapping("/users")
    public String saveStaff(@ModelAttribute Staff staff) {
        staffService.saveStaff(staff);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("staff", staffService.getStaffById(id));
        return "staff-form";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/audit-log")
    public String showAuditLog(Model model) {
        model.addAttribute("logs", auditLogService.getAllLogs());
        return "audit-log";
    }
}
