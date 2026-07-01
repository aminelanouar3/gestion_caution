package com.poste.gestion_caution.service;

import com.poste.gestion_caution.dto.CautionSnapshot;
import com.poste.gestion_caution.entity.Caution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    public CautionSnapshot buildSnapshot(Caution c) {

        if (c == null) {
            return null;
        }

        CautionSnapshot snapshot = new CautionSnapshot();

        // =========================
        // BASIC FIELDS
        // =========================
        snapshot.setCodeInterne(c.getCodeInterne());
        snapshot.setReference(c.getReference());
        snapshot.setMontant(c.getMontant());
        snapshot.setEtat(c.getEtat());

        // =========================
        // DATES (SAFE)
        // =========================
        snapshot.setDate(c.getDate());
        snapshot.setDateMainLevee(c.getDateMainLevee());
        snapshot.setDateRestitution(c.getDateRestitution());

        // =========================
        // RELATIONS → STRING ONLY
        // =========================
        snapshot.setBanque(
                c.getBanque() != null ? c.getBanque().getLibelle() : null
        );

        snapshot.setFournisseur(
                c.getFournisseur() != null ? c.getFournisseur().getLibelle() : null
        );

        snapshot.setOrdonnateur(
                c.getOrdonnateur() != null ? c.getOrdonnateur().getLibelle() : null
        );

        // =========================
        // TEXT FIELDS
        // =========================
        snapshot.setRemarque(c.getRemarque());

        return snapshot;
    }
}