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
    public String list(@RequestParam(required = false) Integer code, Model model) {

        if (code != null) {
            model.addAttribute("ordonnateurs", service.findByCode(code));
        } else {
            model.addAttribute("ordonnateurs", service.getAll());
        }

        model.addAttribute("code", code);

        return "ordonnateurs/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("ordonnateur", new Ordonnateur());
        return "ordonnateurs/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute Ordonnateur ordonnateur) {
        service.save(ordonnateur);
        return "redirect:/ordonnateurs";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/ordonnateurs";
    }

    @GetMapping("/edit/{code}")
    public String editForm(@PathVariable Integer code, Model model) {

        model.addAttribute("ordonnateur", service.findById(code));

        return "ordonnateurs/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Ordonnateur ordonnateur) {

        service.save(ordonnateur);

        return "redirect:/ordonnateurs";
    }
}