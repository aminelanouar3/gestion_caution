package com.poste.gestion_caution.repository;

import com.poste.gestion_caution.entity.Ordonnateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdonnateurRepository extends JpaRepository<Ordonnateur, Integer> {
}