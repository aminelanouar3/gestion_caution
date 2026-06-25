package com.poste.gestion_caution.service;

import com.poste.gestion_caution.entity.Banque;
import com.poste.gestion_caution.repository.BanqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BanqueService {

    private final BanqueRepository repo;

    public List<Banque> getByCode(Integer code) {
        if (code == null) return getAll();
        return repo.findByCode(code);
    }

    public List<Banque> getAll() {
        return repo.findAll();
    }

    public Banque save(Banque banque) {
        return repo.save(banque);
    }

    public Banque findById(Integer id) {
        return repo.findById(id).orElseThrow();
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}