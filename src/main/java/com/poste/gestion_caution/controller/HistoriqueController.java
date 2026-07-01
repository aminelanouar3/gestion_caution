package com.poste.gestion_caution.controller;

import com.poste.gestion_caution.repository.HistoriqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/historique")
public class HistoriqueController {

    private final HistoriqueRepository repo;

    // 🌍 GLOBAL HISTORY
    @GetMapping
    public String globalHistory(Model model) {

        model.addAttribute("historiques",
                repo.findAllByOrderByDateActionDesc());

        return "historique/list";
    }

    // 📄 PER CAUTION HISTORY
    @GetMapping("/caution/{id}")
    public String historyByCaution(@PathVariable Long id,
                                   Model model) {

        model.addAttribute("historiques",
                repo.findByCodeInterneOrderByDateActionDesc(id));

        model.addAttribute("codeInterne", id);

        return "historique/by-caution";
    }
}