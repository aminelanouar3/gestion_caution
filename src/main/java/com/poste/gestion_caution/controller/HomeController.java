package com.poste.gestion_caution.controller;

import com.poste.gestion_caution.service.CautionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CautionService cautionService;

    @GetMapping("/home")
    public String home(Model model) {

        model.addAttribute("title", "Accueil");

        model.addAttribute("total", cautionService.totalCautions());
        model.addAttribute("enCours", cautionService.totalEnCours());
        model.addAttribute("mainLevee", cautionService.totalMainLevee());
        model.addAttribute("restitue", cautionService.totalRestitue());
        model.addAttribute("montantTotal", cautionService.montantTotal());

        model.addAttribute("dernieres",
                cautionService.dernieresCautions());

        return "home";
    }
}
