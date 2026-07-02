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
    public String globalHistory(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) Integer user,
            @RequestParam(required = false) Long codeInterne,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate date,
            Model model) {

        var historiques = repo.findAllByOrderByDateActionDesc();

        // -------------------------
        // FILTERS (in memory for now)
        // -------------------------
        if (action != null && !action.isBlank()) {
            historiques = historiques.stream()
                    .filter(h -> action.equalsIgnoreCase(h.getAct()))
                    .toList();
        }

        if (user != null) {
            historiques = historiques.stream()
                    .filter(h -> user.equals(h.getUtilisateurMatricule()))
                    .toList();
        }

        if (codeInterne != null) {
            historiques = historiques.stream()
                    .filter(h -> codeInterne.equals(h.getCodeInterne()))
                    .toList();
        }

        if (date != null) {
            historiques = historiques.stream()
                    .filter(h -> h.getDateAction() != null &&
                            h.getDateAction().toLocalDate().equals(date))
                    .toList();
        }

        // SNAPSHOT parsing
        var mapper = new com.fasterxml.jackson.databind.ObjectMapper()
                .findAndRegisterModules();

        for (var h : historiques) {
            try {
                if (h.getSnapshot() != null) {
                    h.setSnapshotObject(
                            mapper.readValue(h.getSnapshot(),
                                    com.poste.gestion_caution.dto.CautionSnapshot.class)
                    );
                }
            } catch (Exception e) {
                h.setSnapshotObject(null);
            }
        }

        model.addAttribute("historiques", historiques);

        // keep filters in UI
        model.addAttribute("action", action);
        model.addAttribute("user", user);
        model.addAttribute("codeInterne", codeInterne);
        model.addAttribute("date", date);

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