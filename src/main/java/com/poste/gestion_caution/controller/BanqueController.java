package com.poste.gestion_caution.controller;

import com.poste.gestion_caution.entity.Banque;
import com.poste.gestion_caution.service.BanqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/banques")
@RequiredArgsConstructor
public class BanqueController {

    private final BanqueService service;

    @GetMapping
    public String list(@RequestParam(required = false) Integer code,
                       Model model) {

        if (code != null) {
            model.addAttribute("banques", service.getByCode(code));
        } else {
            model.addAttribute("banques", service.getAll());
        }

        return "banques/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("banque", new Banque());
        return "banques/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute Banque banque) {
        service.save(banque);
        return "redirect:/banques";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/banques";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("banque", service.findById(id));
        return "banques/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Banque banque) {
        service.save(banque);
        return "redirect:/banques";
    }
}