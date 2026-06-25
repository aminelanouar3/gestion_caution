package com.poste.gestion_caution.repository;

import com.poste.gestion_caution.entity.Fournisseur;
import com.poste.gestion_caution.entity.Ordonnateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdonnateurRepository extends JpaRepository<Ordonnateur, Integer> {
    List<Ordonnateur> findByCode(Integer code);
}