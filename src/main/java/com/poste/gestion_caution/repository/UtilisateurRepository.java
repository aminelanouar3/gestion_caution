package com.poste.gestion_caution.repository;

import com.poste.gestion_caution.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    Optional<Utilisateur> findByMatricule(Integer matricule);

}