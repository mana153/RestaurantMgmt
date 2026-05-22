package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.Restaurant;
import com.lodging.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/restaurants")
    public String restaurants(Model model){

        model.addAttribute("restaurants",
                restaurantRepository.findAll());

        model.addAttribute("restaurant",
                new Restaurant());

        return "restaurants";
    }

    @PostMapping("/saveRestaurant")
    public String saveRestaurant(@ModelAttribute Restaurant restaurant) {

        restaurantRepository.save(restaurant);

        return "redirect:/restaurants";
    }

    @GetMapping("/deleteRestaurant/{id}")
    public String deleteRestaurant(@PathVariable Long id) {

        restaurantRepository.deleteById(id);

        return "redirect:/restaurants";
    }

    @GetMapping("/editRestaurant/{id}")
    public String editRestaurant(@PathVariable Long id,
                                 Model model){

        Restaurant restaurant =
                restaurantRepository.findById(id).orElse(null);

        model.addAttribute("restaurant",
                restaurant);

        return "edit-restaurant";
    }

    @GetMapping("/rooms")
    public String rooms() {
        return "rooms";
    }

    @GetMapping("/dining")
    public String dining() {
        return "dining";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}

