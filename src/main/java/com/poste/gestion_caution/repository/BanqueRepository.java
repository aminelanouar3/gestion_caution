package com.poste.gestion_caution.repository;

import com.poste.gestion_caution.entity.Banque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BanqueRepository extends JpaRepository<Banque, Integer> {
    List<Banque> findByCode(Integer code);
}