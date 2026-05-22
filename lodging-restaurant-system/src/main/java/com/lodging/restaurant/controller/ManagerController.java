package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.Shift;
import com.lodging.restaurant.service.FolioService;
import com.lodging.restaurant.service.RoomService;
import com.lodging.restaurant.service.ShiftService;
import com.lodging.restaurant.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private FolioService folioService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private StaffService staffService;

    @GetMapping("/reports/occupancy")
    public String showOccupancyReport(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "occupancy-report";
    }

    @GetMapping("/reports/revenue")
    public String showRevenueReport(Model model) {
        double totalRevenue = folioService.getAllFolios().stream()
                .mapToDouble(folio -> folio.getTotalBill() + folio.getReservation().getRoom().getPrice())
                .sum();
        model.addAttribute("totalRevenue", totalRevenue);
        return "revenue-report";
    }

    @GetMapping("/scheduling")
    public String showScheduling(Model model) {
        model.addAttribute("shifts", shiftService.getAllShifts());
        model.addAttribute("staff", staffService.getAllStaff());
        model.addAttribute("shift", new Shift());
        return "staff-scheduling";
    }

    @PostMapping("/scheduling")
    public String saveShift(@ModelAttribute Shift shift) {
        shiftService.saveShift(shift);
        return "redirect:/manager/scheduling";
    }

    @GetMapping("/scheduling/delete/{id}")
    public String deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return "redirect:/manager/scheduling";
    }
}
