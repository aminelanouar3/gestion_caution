package com.poste.gestion_caution.controller;

import com.poste.gestion_caution.dto.CreateUserRequest;
import com.poste.gestion_caution.dto.UpdateUserRequest;
import com.poste.gestion_caution.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserManagementService userManagementService;

    // ================= LIST USERS =================
    @GetMapping
    public String listUsers(@RequestParam(required = false) Integer matricule, Model model) {

        if (matricule != null) {
            model.addAttribute("users",
                    userManagementService.findByMatricule(matricule));
        } else {
            model.addAttribute("users", userManagementService.getAllUsers());
        }

        return "users/list";
    }

    // ================= SHOW CREATE FORM =================
    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute("user", new CreateUserRequest());
        return "users/create";
    }

    // ================= HANDLE CREATE =================
    @PostMapping("/create")
    public String createUser(@ModelAttribute CreateUserRequest request,
                             RedirectAttributes redirectAttributes) {

        var createdUser = userManagementService.createUser(request);

        redirectAttributes.addFlashAttribute("createdUserId", createdUser.getMatricule());

        return "redirect:/users";
    }

    // ================= SHOW EDIT FORM =================
    @GetMapping("/edit/{matricule}")
    public String showEditForm(@PathVariable Integer matricule, Model model) {

        model.addAttribute("user",
                userManagementService.findByMatricule(matricule));

        return "users/edit";
    }

    // ================= HANDLE UPDATE =================
    @PostMapping("/edit/{matricule}")
    public String updateUser(@PathVariable Integer matricule,
                             @ModelAttribute UpdateUserRequest request) {

        userManagementService.updateUser(matricule, request);
        return "redirect:/users";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/home";
    }
}