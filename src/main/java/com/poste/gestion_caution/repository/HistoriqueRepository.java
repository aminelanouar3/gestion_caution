package com.poste.gestion_caution.repository;

import com.poste.gestion_caution.entity.Historique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueRepository extends JpaRepository<Historique, Long> {

    List<Historique> findByCodeInterneOrderByDateActionDesc(Long codeInterne);

    List<Historique> findAllByOrderByDateActionDesc();
}