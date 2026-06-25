package com.poste.gestion_caution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("hideNavbar", true);
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}