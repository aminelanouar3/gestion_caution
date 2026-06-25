package com.poste.gestion_caution.controller;

import com.poste.gestion_caution.entity.Fournisseur;
import com.poste.gestion_caution.service.FournisseurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fournisseurs")
@RequiredArgsConstructor
public class FournisseurController {

    private final FournisseurService service;

    @GetMapping
    public String list(@RequestParam(required = false) Integer code, Model model) {

        if (code != null) {
            model.addAttribute("fournisseurs", service.findByCode(code));
        } else {
            model.addAttribute("fournisseurs", service.getAll());
        }

        model.addAttribute("code", code);

        return "fournisseurs/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("fournisseur", new Fournisseur());
        return "fournisseurs/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute Fournisseur f) {
        service.save(f);
        return "redirect:/fournisseurs";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/fournisseurs";
    }
    @GetMapping("/edit/{code}")
    public String editForm(@PathVariable Integer code, Model model) {

        model.addAttribute("fournisseur", service.findById(code));

        return "fournisseurs/edit";
    }
    @PostMapping("/update")
    public String update(@ModelAttribute Fournisseur fournisseur) {

        service.save(fournisseur);

        return "redirect:/fournisseurs";
    }
}