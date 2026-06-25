package com.poste.gestion_caution.service;

import com.poste.gestion_caution.entity.Ordonnateur;
import com.poste.gestion_caution.repository.OrdonnateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdonnateurService {

    private final OrdonnateurRepository repo;

    public List<Ordonnateur> getAll() {
        return repo.findAll();
    }

    public Ordonnateur save(Ordonnateur o) {
        return repo.save(o);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}