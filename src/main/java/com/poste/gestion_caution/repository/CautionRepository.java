package com.poste.gestion_caution.repository;

import com.poste.gestion_caution.entity.Caution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CautionRepository extends JpaRepository<Caution, Long> {
}