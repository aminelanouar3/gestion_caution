package com.poste.gestion_caution.controller;

import com.poste.gestion_caution.dto.CautionSnapshot;
import com.poste.gestion_caution.entity.HistoriqueCaution;
import com.poste.gestion_caution.repository.HistoriqueCautionRepository;
import com.poste.gestion_caution.service.SnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cautions")
public class HistoriqueController {

    private final HistoriqueCautionRepository repository;
    private final SnapshotService snapshotService;

    @GetMapping("/{id}/historique")
    public String historique(@PathVariable Long id, Model model) {

        List<HistoriqueCaution> historiques =
                repository.findByCautionCodeInterne(id);

        Map<Long, CautionSnapshot> snapshots = new HashMap<>();

        for (HistoriqueCaution h : historiques) {

            if (h.getOldValue() != null) {
                snapshots.put(
                        h.getId(),
                        snapshotService.readSnapshot(h.getOldValue())
                );
            }

        }

        model.addAttribute("historiques", historiques);
        model.addAttribute("snapshots", snapshots);
        model.addAttribute("cautionId", id);

        return "cautionS/historique";
    }

}