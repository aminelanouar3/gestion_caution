package com.poste.gestion_caution.repository;

import com.poste.gestion_caution.entity.HistoriqueCaution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueCautionRepository extends JpaRepository<HistoriqueCaution, Long> {

    List<HistoriqueCaution> findByCautionCodeInterne(Long id);
}