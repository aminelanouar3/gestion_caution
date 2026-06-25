package com.poste.gestion_caution.controller;

import com.poste.gestion_caution.entity.Ordonnateur;
import com.poste.gestion_caution.service.OrdonnateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ordonnateurs")
@RequiredArgsConstructor
public class OrdonnateurController {

    private final OrdonnateurService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("ordonnateurs", service.getAll());
        return "ordonnateurs/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("ordonnateur", new Ordonnateur());
        return "ordonnateurs/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute Ordonnateur o) {
        service.save(o);
        return "redirect:/ordonnateurs";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/ordonnateurs";
    }
}