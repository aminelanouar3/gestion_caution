package com.poste.gestion_caution.service;

import com.poste.gestion_caution.entity.Banque;
import com.poste.gestion_caution.entity.Fournisseur;
import com.poste.gestion_caution.repository.FournisseurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FournisseurService {

    private final FournisseurRepository repo;

    public List<Fournisseur> findByCode(Integer code) {
        if (code == null) return getAll();
        return repo.findByCode(code);
    }

    public List<Fournisseur> getAll() {
        return repo.findAll();
    }

    public Fournisseur findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public Fournisseur save(Fournisseur f) {
        return repo.save(f);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}