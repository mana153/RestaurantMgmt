package com.lodging.restaurant.controller;

import com.lodging.restaurant.model.User;
import com.lodging.restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String signupPage(Model model) {

        model.addAttribute("user", new User());

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user) {

        userRepository.save(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        jakarta.servlet.http.HttpSession session) {

        User user = userRepository.findByEmail(email);

        if (user != null &&
                user.getPassword().equals(password)) {

            session.setAttribute("loggedInUser",
                    user.getFullName());

            return "redirect:/";
        }

        model.addAttribute("error",
                "Invalid Email or Password");

        return "login";
    }
}