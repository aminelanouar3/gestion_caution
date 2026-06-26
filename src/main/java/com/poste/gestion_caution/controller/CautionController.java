package com.poste.gestion_caution.controller;

import com.poste.gestion_caution.entity.Caution;
import com.poste.gestion_caution.entity.EtatCaution;
import com.poste.gestion_caution.repository.BanqueRepository;
import com.poste.gestion_caution.repository.FournisseurRepository;
import com.poste.gestion_caution.repository.OrdonnateurRepository;
import com.poste.gestion_caution.service.CautionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/cautions")
@RequiredArgsConstructor
public class CautionController {

    private final BanqueRepository banqueRepo;
    private final FournisseurRepository fournisseurRepo;
    private final OrdonnateurRepository ordonnateurRepo;
    private final CautionService service;

    // -----------------------
    // LIST
    // -----------------------
    @GetMapping
    public String list(
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) EtatCaution etat,
            Model model) {

        model.addAttribute("cautions",
                service.search(reference, dateFrom, dateTo, etat));

        return "cautions/list";
    }

    // -----------------------
    // CREATE FORM
    // -----------------------
    @GetMapping("/new")
    public String createForm(Model model) {

        model.addAttribute("caution", new Caution());

        model.addAttribute("banques", banqueRepo.findAll());
        model.addAttribute("fournisseurs", fournisseurRepo.findAll());
        model.addAttribute("ordonnateurs", ordonnateurRepo.findAll());

        return "cautions/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Caution c,
                       @RequestParam Integer banqueId,
                       @RequestParam Integer fournisseurId,
                       @RequestParam Integer ordonnateurId) {

        service.save(c, banqueId, fournisseurId, ordonnateurId);

        return "redirect:/cautions";
    }
    // -----------------------
    // EDIT FORM
    // -----------------------
    @GetMapping("/admin/edit/{id}")
    public String adminEditForm(@PathVariable Long id, Model model) {

        model.addAttribute("caution", service.findById(id));
        model.addAttribute("banques", banqueRepo.findAll());
        model.addAttribute("fournisseurs", fournisseurRepo.findAll());
        model.addAttribute("ordonnateurs", ordonnateurRepo.findAll());

        return "cautions/admin-edit";
    }
    @PostMapping("/admin/update")
    public String adminUpdate(@ModelAttribute Caution c,
                              @RequestParam Integer banqueId,
                              @RequestParam Integer fournisseurId,
                              @RequestParam Integer ordonnateurId) {

        service.save(c, banqueId, fournisseurId, ordonnateurId);

        return "redirect:/cautions/gestion";
    }

    // -----------------------
    // DELETE
    // -----------------------
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/cautions/gestion";
    }

    // -----------------------
    // CHANGE STATE
    // -----------------------
    @PostMapping("/{id}/state")
    public String changeState(@PathVariable Long id,
                              @RequestParam EtatCaution state,
                              @RequestParam(required = false) LocalDate dateMainLevee,
                              @RequestParam(required = false) LocalDate dateRestitution) {

        service.changeState(id, state, dateMainLevee, dateRestitution);
        return "redirect:/cautions/gestion";
    }
    @GetMapping("/gestion")
    public String gestion(@RequestParam(required = false) Long code,
                          Model model) {

        model.addAttribute("cautions",
                service.searchByCode(code));

        return "cautions/gestion";
    }

}