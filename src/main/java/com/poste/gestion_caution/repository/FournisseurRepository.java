package com.poste.gestion_caution.repository;

import com.poste.gestion_caution.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {
    List<Fournisseur> findByCode(Integer code);

}