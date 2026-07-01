package com.poste.gestion_caution.repository;

import com.poste.gestion_caution.entity.Caution;
import com.poste.gestion_caution.entity.EtatCaution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CautionRepository extends JpaRepository<Caution, Long> {

    long countByEtat(EtatCaution etat);

    @Query("SELECT COALESCE(SUM(c.montant),0) FROM Caution c")
    Double getTotalMontant();

    List<Caution> findTop5ByOrderByDateDesc();
    List<Caution> findAllByOrderByCodeInterneDesc();
}