package com.lodgingrestaurant.controllers;

import com.lodgingrestaurant.models.*;
import com.lodgingrestaurant.repositories.RoomRepository;
import com.lodgingrestaurant.repositories.OrderRepository;
import com.lodgingrestaurant.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PartnerApiController {

    private final PartnerApiService partnerApiService;
    private final MenuItemService menuItemService;
    private final RoomRepository roomRepository;
    private final BookingService bookingService;
    private final GuestService guestService;
    private final OrderRepository orderRepository;

    @Autowired
    public PartnerApiController(PartnerApiService partnerApiService,
                                MenuItemService menuItemService,
                                RoomRepository roomRepository,
                                BookingService bookingService,
                                GuestService guestService,
                                OrderRepository orderRepository) {
        this.partnerApiService = partnerApiService;
        this.menuItemService = menuItemService;
        this.roomRepository = roomRepository;
        this.bookingService = bookingService;
        this.guestService = guestService;
        this.orderRepository = orderRepository;
    }

    // ==========================================
    // 1. DEVELOPER INTEGRATION DASHBOARD (HTML VIEW)
    // ==========================================
    // GET /api/dashboard - Renders the Zomato/Swiggy developer log dashboard
    @GetMapping("/api/dashboard")
    public String showApiDashboard(Model model) {
        model.addAttribute("totalRequests", partnerApiService.getTotalRequests());
        model.addAttribute("listingRequests", partnerApiService.getListingRequests());
        model.addAttribute("createSuccess", partnerApiService.getCreateSuccess());
        model.addAttribute("createFailure", partnerApiService.getCreateFailure());
        model.addAttribute("updateSuccess", partnerApiService.getUpdateSuccess());
        model.addAttribute("updateFailure", partnerApiService.getUpdateFailure());
        model.addAttribute("cancelSuccess", partnerApiService.getCancelSuccess());
        model.addAttribute("cancelFailure", partnerApiService.getCancelFailure());
        model.addAttribute("recentActivity", partnerApiService.getRecentActivity());

        // Quick Database Counts to make dashboard awesome
        model.addAttribute("activeRoomsCount", roomRepository.findByIsAvailable(true).size());
        model.addAttribute("totalRoomsCount", roomRepository.findAll().size());
        model.addAttribute("activeBookingsCount", bookingService.getAllBookings().size());
        model.addAttribute("menuItemsCount", menuItemService.getAllMenuItems().size());
        
        // Sum total partner order sales
        double totalPartnerSales = orderRepository.findAll().stream()
                .filter(o -> !"IN-HOUSE".equalsIgnoreCase(o.getPartnerName()))
                .mapToDouble(Order::getTotalAmount)
                .sum();
        model.addAttribute("partnerSales", totalPartnerSales);

        model.addAttribute("pageTitle", "Third-Party Integration Hub");
        return "dashboard/dashboard";
    }

    // ==========================================
    // 2. THIRD-PARTY REST PARTNER API ENDPOINTS (JSON)
    // ==========================================

    // GET /api/partners/listings - Returns available rooms & menu items
    @GetMapping("/api/partners/listings")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAvailableListings() {
        partnerApiService.incrementListingRequests();
        
        List<Room> availableRooms = roomRepository.findByIsAvailable(true);
        List<MenuItem> availableMenu = menuItemService.getAllMenuItems().stream()
                .filter(MenuItem::isAvailable)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("timestamp", LocalDateTime.now());
        response.put("availableRoomsCount", availableRooms.size());
        response.put("availableRooms", availableRooms);
        response.put("availableMenuItemsCount", availableMenu.size());
        response.put("availableMenuItems", availableMenu);

        return ResponseEntity.ok(response);
    }

    // POST /api/partners/create - Receives a partner order/booking creation request
    @PostMapping("/api/partners/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createPartnerTransaction(@RequestBody Map<String, Object> request) {
        String partner = (String) request.getOrDefault("partnerName", "UNKNOWN");
        String type = (String) request.getOrDefault("type", "ORDER"); // ORDER or BOOKING
        
        Map<String, Object> response = new HashMap<>();

        try {
            if ("BOOKING".equalsIgnoreCase(type)) {
                // Parse booking parameters
                Long guestId = Long.valueOf(request.get("guestId").toString());
                Long roomId = Long.valueOf(request.get("roomId").toString());
                LocalDate checkIn = LocalDate.parse(request.get("checkInDate").toString());
                LocalDate checkOut = LocalDate.parse(request.get("checkOutDate").toString());

                Guest guest = guestService.getGuestById(guestId);
                Room room = roomRepository.findById(roomId).orElse(null);

                if (guest == null || room == null) {
                    partnerApiService.incrementCreateFailure(partner, "BOOKING", "Guest or Room entity not found");
                    response.put("status", "FAILURE");
                    response.put("message", "Guest ID or Room ID is invalid");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                if (!room.isAvailable()) {
                    partnerApiService.incrementCreateFailure(partner, "BOOKING", "Room " + room.getRoomNumber() + " is already booked");
                    response.put("status", "FAILURE");
                    response.put("message", "Selected Room is not available");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }

                Booking booking = new Booking(guest, room, checkIn, checkOut, 0.0, "CONFIRMED");
                Booking savedBooking = bookingService.saveBooking(booking);

                partnerApiService.incrementCreateSuccess(partner, "BOOKING", "Booking #" + savedBooking.getId() + " (Room " + room.getRoomNumber() + ")");
                
                response.put("status", "SUCCESS");
                response.put("transactionId", savedBooking.getId());
                response.put("booking", savedBooking);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);

            } else {
                // Default: Restaurant Order (Zomato/Swiggy)
                String roomNo = (String) request.get("roomNumber"); // Can be null for direct deliveries
                Double amount = Double.valueOf(request.get("totalAmount").toString());

                Order order = new Order(roomNo, LocalDateTime.now(), amount, "PENDING", partner.toUpperCase());
                Order savedOrder = orderRepository.save(order);

                partnerApiService.incrementCreateSuccess(partner, "ORDER", "Order #" + savedOrder.getId() + " of Amount ₹" + amount);

                response.put("status", "SUCCESS");
                response.put("transactionId", savedOrder.getId());
                response.put("order", savedOrder);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        } catch (Exception e) {
            partnerApiService.incrementCreateFailure(partner, type, "Payload parsing error: " + e.getMessage());
            response.put("status", "FAILURE");
            response.put("message", "Invalid request body format: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // PUT /api/partners/update/{id} - Update booking or order status
    @PutMapping("/api/partners/update/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updatePartnerTransaction(@PathVariable("id") Long id, @RequestBody Map<String, Object> request) {
        String partner = (String) request.getOrDefault("partnerName", "UNKNOWN");
        String type = (String) request.getOrDefault("type", "ORDER"); // ORDER or BOOKING
        String newStatus = (String) request.get("status");

        Map<String, Object> response = new HashMap<>();

        if (newStatus == null || newStatus.isEmpty()) {
            partnerApiService.incrementUpdateFailure(partner, type, id, "Missing field: status");
            response.put("status", "FAILURE");
            response.put("message", "Status field is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            if ("BOOKING".equalsIgnoreCase(type)) {
                Booking booking = bookingService.getBookingById(id);
                if (booking == null) {
                    partnerApiService.incrementUpdateFailure(partner, "BOOKING", id, "Booking not found");
                    response.put("status", "FAILURE");
                    response.put("message", "Booking ID not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }

                bookingService.updateBookingStatus(id, newStatus);
                partnerApiService.incrementUpdateSuccess(partner, "BOOKING", id);
                
                response.put("status", "SUCCESS");
                response.put("message", "Booking status updated successfully to " + newStatus);
                return ResponseEntity.ok(response);
            } else {
                // Restaurant Order
                Order order = orderRepository.findById(id).orElse(null);
                if (order == null) {
                    partnerApiService.incrementUpdateFailure(partner, "ORDER", id, "Order not found");
                    response.put("status", "FAILURE");
                    response.put("message", "Order ID not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }

                order.setStatus(newStatus.toUpperCase());
                orderRepository.save(order);
                partnerApiService.incrementUpdateSuccess(partner, "ORDER", id);

                response.put("status", "SUCCESS");
                response.put("message", "Order status updated successfully to " + newStatus);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            partnerApiService.incrementUpdateFailure(partner, type, id, "Server error: " + e.getMessage());
            response.put("status", "FAILURE");
            response.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DELETE /api/partners/cancel/{id} - Cancels booking or order
    @DeleteMapping("/api/partners/cancel/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelPartnerTransaction(@PathVariable("id") Long id, @RequestBody Map<String, Object> request) {
        String partner = (String) request.getOrDefault("partnerName", "UNKNOWN");
        String type = (String) request.getOrDefault("type", "ORDER"); // ORDER or BOOKING
        
        Map<String, Object> response = new HashMap<>();

        try {
            if ("BOOKING".equalsIgnoreCase(type)) {
                Booking booking = bookingService.getBookingById(id);
                if (booking == null) {
                    partnerApiService.incrementCancelFailure(partner, "BOOKING", id, "Booking not found");
                    response.put("status", "FAILURE");
                    response.put("message", "Booking ID not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }

                bookingService.updateBookingStatus(id, "CANCELLED");
                partnerApiService.incrementCancelSuccess(partner, "BOOKING", id);

                response.put("status", "SUCCESS");
                response.put("message", "Booking cancelled successfully");
                return ResponseEntity.ok(response);
            } else {
                // Restaurant Order
                Order order = orderRepository.findById(id).orElse(null);
                if (order == null) {
                    partnerApiService.incrementCancelFailure(partner, "ORDER", id, "Order not found");
                    response.put("status", "FAILURE");
                    response.put("message", "Order ID not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }

                order.setStatus("CANCELLED");
                orderRepository.save(order);
                partnerApiService.incrementCancelSuccess(partner, "ORDER", id);

                response.put("status", "SUCCESS");
                response.put("message", "Order cancelled successfully");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            partnerApiService.incrementCancelFailure(partner, type, id, "Server error: " + e.getMessage());
            response.put("status", "FAILURE");
            response.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
